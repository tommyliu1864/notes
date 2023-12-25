package com.hmall.api.config;

import com.hmall.common.utils.UserContext;
import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;

public class DefaultFeignConfig {

    // 设置open feign的日志级别，默认open feign是不显示日志的
    @Bean
    public Logger.Level feignLogLevel() {
        return Logger.Level.FULL;
    }

    // 微服务之间调用是基于open feign的，需要在服务调用的时候传递当前用户信息
    // 通过拦截器，让open feign的每一个请求，自带用户信息请求头
    @Bean
    public RequestInterceptor userInfoRequestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                // 获取登录用户
                // 这里举例，当订单服务调用购物车服务，那么getUser就是从订单服务的当前线程获取当前用户信息
                // 然后，放入请求头，由订单服务发起对购物车的请求调用，那么接下来在购物车服务的mvc拦截器中，就可以获取到请求头携带的用户信息了
                Long userId = UserContext.getUser();
                if (userId == null) {
                    // 如果为空则直接跳过
                    return;
                }
                // 如果不为空则放入请求头中，传递给下游服务
                requestTemplate.header("user-info", userId.toString());
            }
        };
    }

}
