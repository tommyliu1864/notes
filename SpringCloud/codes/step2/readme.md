

这一阶段主要的技术点集中在nacos配置管理

1. 配置共享
   - 将jdbc、日志、swagger配置抽取出来，交给nacos统一做配置管理

2. 配置热更新
   - cart-service中的购物车商品数量限制，由nacos管理
   - 支持热更新

3. 动态路由
   - gateway-service的路由信息有nacos配置中心管理
   - 支持动态路由
