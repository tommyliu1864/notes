package com.hmall.trade.listener;

import com.hmall.trade.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.hmall.common.constants.MqConstants;

/**
 * 接受MQ异步消息，更新订单状态
 */
@Component
@RequiredArgsConstructor
public class PayStatusListener {

    private final IOrderService orderService;

    @RabbitListener(bindings = @QueueBinding(
                    value = @Queue(name = MqConstants.ORDER_PAY_QUEUE, durable = "true"),
                    exchange = @Exchange(name = MqConstants.ORDER_PAY_EXCHANGE, type = ExchangeTypes.TOPIC),
                    key = MqConstants.ORDER_PAY_ROUTING_KEY
    ))
    public void listenPaySuccess(Long orderId){
        orderService.markOrderPaySuccess(orderId);
    }

}
