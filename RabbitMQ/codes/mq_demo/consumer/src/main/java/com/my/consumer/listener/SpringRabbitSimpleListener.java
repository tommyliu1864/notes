package com.my.consumer.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.Map;

@Component
public class SpringRabbitSimpleListener {

    @RabbitListener(queues = "simple.queue")
    public void listenSimpleQueueMessage(String msg){
        System.out.println("消费者接收到的消息：" + msg);
        if (true){
            throw new RuntimeException("故意的");
        }
        System.out.println("消息处理完成");
    }


    @RabbitListener(queues = "work.queue")
    public void listenWorkQueue1(String msg) throws InterruptedException {
        System.out.println("消费者1接收到的消息：【" + msg + "】" + LocalTime.now());
        Thread.sleep(20);
    }

    @RabbitListener(queues = "work.queue")
    public void listenWorkQueue2(String msg) throws InterruptedException {
        System.out.println("消费者2接收到的消息：【" + msg + "】" + LocalTime.now());
        Thread.sleep(200);
    }

    @RabbitListener(queues = "object.queue")
    public void listenObjectQueue(Map<String, Object> msg) {
        System.out.println("消费者接收到object.queue的消息：【" + msg + "】");
    }

}
