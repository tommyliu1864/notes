package com.hmall.trade.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmall.api.client.CartClient;
import com.hmall.api.client.ItemClient;
import com.hmall.api.dto.ItemDTO;
import com.hmall.api.dto.ItemStockDTO;
import com.hmall.api.dto.OrderDetailDTO;
import com.hmall.common.constants.MqConstants;
import com.hmall.common.domain.MultiDelayMessage;
import com.hmall.common.exception.BadRequestException;
import com.hmall.common.utils.UserContext;
import com.hmall.trade.domain.dto.OrderFormDTO;
import com.hmall.trade.domain.po.Order;
import com.hmall.trade.domain.po.OrderDetail;
import com.hmall.trade.mapper.OrderMapper;
import com.hmall.trade.service.IOrderDetailService;
import com.hmall.trade.service.IOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2023-05-05
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    private final IOrderDetailService detailService;

    private final ItemClient itemClient;

    private final CartClient cartClient;

    private final RabbitTemplate rabbitTemplate;

    @Override
    @Transactional
    public Long createOrder(OrderFormDTO orderFormDTO) {
        // 1.订单数据
        Order order = new Order();
        // 1.1.查询商品
        List<OrderDetailDTO> detailDTOS = orderFormDTO.getDetails();
        // 1.2.获取商品id和数量的Map
        Map<Long, Integer> itemNumMap = detailDTOS.stream()
                .collect(Collectors.toMap(OrderDetailDTO::getItemId, OrderDetailDTO::getNum));
        Set<Long> itemIds = itemNumMap.keySet();
        // 1.3.查询商品
        List<ItemDTO> items = itemClient.queryItemByIds(itemIds);
        if (items == null || items.size() < itemIds.size()) {
            throw new BadRequestException("商品不存在");
        }
        // 1.4.基于商品价格、购买数量计算商品总价：totalFee
        int total = 0;
        for (ItemDTO item : items) {
            total += item.getPrice() * itemNumMap.get(item.getId());
        }
        order.setTotalFee(total);
        // 1.5.其它属性
        order.setPaymentType(orderFormDTO.getPaymentType());
        order.setUserId(UserContext.getUser());
        order.setStatus(1);
        // 1.6.将Order写入数据库order表中
        save(order);

        // 2.保存订单详情
        List<OrderDetail> details = buildDetails(order.getId(), items, itemNumMap);
        detailService.saveBatch(details);

        // 3.清理购物车商品
        cartClient.deleteCartItemByIds(itemIds);

        // 4.扣减库存
        try {
            itemClient.deductStock(detailDTOS);
        } catch (Exception e) {
            throw new RuntimeException("库存不足！");
        }

        // 5.发送MQ消息，延迟查询订单支付状态，当订单超时，需要取消订单，并修改库存
        // 5.1 组织消息，延迟时长为：10秒（3次），20秒（3次），1分钟、5分钟、10分钟
        try {
            MultiDelayMessage<Long> msg = MultiDelayMessage.of(order.getId(),
                    10000L,
                    20000L
                    //60000L
                    //300000L,600000L
            );
            // 5.2 发送延迟消息
            rabbitTemplate.convertAndSend(MqConstants.DELAY_EXCHANGE, MqConstants.DELAY_ORDER_ROUTING_KEY, msg);
        } catch (AmqpException e){
            log.error("消息发送异常", e);
        }
        return order.getId();
    }

    @Override
    public void markOrderPaySuccess(Long orderId) {
        // 重复消息，会导致幂等性问题，这里加上状态判断，可以保证幂等
        // 只有订单状态为1（未付款状态），才能更新为2（已付款）
        // UPDATE `order` SET status = ? , pay_time = ? WHERE id = ? AND status = 1
        lambdaUpdate()
                .set(Order::getStatus, 2)
                .set(Order::getPayTime, LocalDateTime.now())
                .eq(Order::getId, orderId)
                .eq(Order::getStatus, 1)
                .update();
        /*Order order = new Order();
        order.setId(orderId);
        order.setStatus(2);
        order.setPayTime(LocalDateTime.now());
        updateById(order);*/
    }

    private List<OrderDetail> buildDetails(Long orderId, List<ItemDTO> items, Map<Long, Integer> numMap) {
        List<OrderDetail> details = new ArrayList<>(items.size());
        for (ItemDTO item : items) {
            OrderDetail detail = new OrderDetail();
            detail.setName(item.getName());
            detail.setSpec(item.getSpec());
            detail.setPrice(item.getPrice());
            detail.setNum(numMap.get(item.getId()));
            detail.setItemId(item.getId());
            detail.setImage(item.getImage());
            detail.setOrderId(orderId);
            details.add(detail);
        }
        return details;
    }

    @Override
    public void cancelOrder(Long orderId) {
        // 1.将订单状态修改为已关闭
        Order order = getById(orderId);
        // 如果订单为未支付状态，修改为关闭状态
        lambdaUpdate()
                .set(order.getStatus() == 1, Order::getStatus, 5)
                .update();
        // 2.恢复订单中已经扣除的库存
        // 2.1 查询出订单列表中的具体商品列表
        List<OrderDetail> list = detailService.lambdaQuery()
                .eq(orderId != null, OrderDetail::getOrderId, orderId)
                .list();
        // 2.2 获取需要更新的商品库存信息
        List<ItemStockDTO> itemStockDTOS = list.stream().map(datailItem -> {
            return new ItemStockDTO(datailItem.getItemId(), datailItem.getNum());
        }).collect(Collectors.toList());
        log.debug("获取需要更新的商品库存信息：{}", itemStockDTOS.toString());
        // 2.3 远程调用item-service，更新商品库存
        itemClient.returnStock(itemStockDTOS);
    }
}
