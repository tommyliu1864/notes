

这一阶段主要的技术点集中在sentinel的服务保护，包括

1. 请求限流，只需在sentinel配置限流策略
2. 线程隔离，需在sentinel配置线程隔离策略，如果需要对feign的远程调用的服务进行线程隔离，feign需要开启sentinel支持
3. 服务熔断，只需在sentinel配置熔断策略，但如果要对服务调用异常进行fallback处理，需要实现FallbackFactory

