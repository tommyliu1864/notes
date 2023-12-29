这一阶段主要集中在运用分布式事务管理seata，对XA和AT模式都进行了实验

1. trade-service 
   - 引入了seata依赖
   - bootstrap.yaml 使用共享配置shared-seata.yaml
   - OrderServiceImpl.createOrder 加上了@GlobalTransactional注解，支持分布式事务
2. cart-service 
   - 引入了seata依赖
   - bootstrap.yaml 使用共享配置shared-seata.yaml
   - 对应方法上开启@Transactional事务支持
3. item-service
   - 引入了seata依赖
   - bootstrap.yaml 使用共享配置shared-seata.yaml
   - 对应方法上开启@Transactional事务支持

