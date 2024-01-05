改造余额支付功能，将支付成功后基于OpenFeign的交易服务的更新订单状态接口的同步调用，改为基于RabbitMQ的异步通知。

- 修改`pay-service`服务下的`com.hmall.pay.service.impl.PayOrderServiceImpl`类中的`tryPayOrderByBalance`方法
- 在trade-service服务中定义一个消息监听类`PayStatusListener `

处理消息的业务逻辑是把订单状态从未支付修改为已支付，这里要保证幂等性，因此我们就可以在执行业务时判断订单状态是否是未支付，如果不是则证明订单已经被处理过，无需重复处理。

- 修改`OrderServiceImpl`中的`markOrderPaySuccess`方法，保证消息的幂等性

改造下单业务，在下单完成后，发送延迟消息，查询支付状态。

- 修改`trade-service`模块的`com.hmall.trade.service.impl.OrderServiceImpl`类的`createOrder`方法，添加消息发送的代码
- 在`trader-service`编写一个监听器，监听延迟消息，查询订单支付状态
- 在`pay-service`模块定义一个接口`queryPayOrderByBizOrderNo`，查询支付状态