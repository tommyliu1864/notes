package com.my.publisher;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DelayedMessageTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testDelayedMessage(){
        String message = "延迟消息";
        rabbitTemplate.convertAndSend("delay.direct", "delay",message, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                // 添加延迟消息属性
                message.getMessageProperties().setDelay(5000);
                return message;
            }
        });
    }

}
