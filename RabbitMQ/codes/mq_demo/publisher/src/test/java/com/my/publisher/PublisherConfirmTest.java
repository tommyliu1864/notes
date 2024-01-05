package com.my.publisher;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Correlation;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
@SpringBootTest
public class PublisherConfirmTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testPublisherConfirm(){
        // 1.创建CorrelationData
        CorrelationData cd = new CorrelationData();
        // 2.给future添加ConfirmCallback
        cd.getFuture().addCallback(new ListenableFutureCallback<CorrelationData.Confirm>() {
            @Override
            public void onFailure(Throwable ex) {
                // 2.1.Future发生异常时的处理逻辑，基本不会触发
                log.error("send message fail", ex);
            }

            @Override
            public void onSuccess(CorrelationData.Confirm result) {
                // 2.2.Future接收到回执的处理逻辑，参数中的result就是回执内容
                if (result.isAck()){ // result.isAck()，boolean类型，true代表ack回执，false 代表 nack回执
                    log.debug("发送消息成功，收到ack");
                }else {
                    // result.getReason()，String类型，返回nack时的异常描述
                    log.error("发送消息失败，收到nack，reason: {}", result.getReason());
                }
            }
        });

        // 3.发送消息
        rabbitTemplate.convertAndSend("hmall.direct", "q", "hello", cd);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
