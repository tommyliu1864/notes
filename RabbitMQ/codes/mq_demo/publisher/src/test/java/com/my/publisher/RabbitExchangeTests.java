package com.my.publisher;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RabbitExchangeTests {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testFanoutExchange(){
        // 交换机名称
        String exchangeName = "hmall.fanout";
        // 消息
        String message = "hello,fanout";
        rabbitTemplate.convertAndSend(exchangeName, "", message);
    }

    @Test
    public void testDirectExchange(){
        // 交换机名称
        String exchangeName = "hmall.direct";
        // 消息
        String message = "红色警报！日本乱排核废水，导致海洋生物变异，惊现哥斯拉！";
        //String message = "最新报道，哥斯拉是居民自治巨型气球，虚惊一场！";
        rabbitTemplate.convertAndSend(exchangeName, "red", message);
    }

    @Test
    public void testTopicExchange(){
        // 交换机名称
        String exchangeName = "hmall.topic";
        // 消息
        String message = "喜报！孙悟空大战哥斯拉，胜!";
        rabbitTemplate.convertAndSend(exchangeName, "japan.news", message);
    }


}
