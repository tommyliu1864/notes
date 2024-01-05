package com.hmall.common.constants;

/**
 * MQ相关的常量
 */
public interface MqConstants {

    // 订单状态相关配置，用于修改订单状态
    String ORDER_PAY_EXCHANGE = "pay.topic";
    String ORDER_PAY_QUEUE = "mark.order.pay.queue";
    String ORDER_PAY_ROUTING_KEY = "pay.success";

    // 延迟消息，用于订单超时取消订单，修改库存
    String DELAY_EXCHANGE = "trade.delay.topic";
    String DELAY_ORDER_QUEUE = "trade.order.delay.queue";
    String DELAY_ORDER_ROUTING_KEY = "order.query";
}
