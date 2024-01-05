package com.my.publisher;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class ObjectMessageTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSendMap(){
        Map<String, Object> msg = new HashMap<>();
        msg.put("name", "柳岩");
        msg.put("age", 21);
        rabbitTemplate.convertAndSend("object.queue", msg);
    }

}
