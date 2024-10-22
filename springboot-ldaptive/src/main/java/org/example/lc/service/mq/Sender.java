package org.example.lc.service.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class Sender implements RabbitTemplate.ConfirmCallback {
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(String topic, String msg) {
        log.info("MqSender.send topic:{}, msg:{}", topic, msg);
        this.amqpTemplate.convertAndSend(topic, msg);
    }

    public void send2(String topic, String msg){
        rabbitTemplate.setConfirmCallback(this);

        System.out.println("callbackSender : i am callback sender");
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        System.out.println("callbackSender UUID: " + correlationData.getId());
        this.rabbitTemplate.convertAndSend("exchange", "topic.message", msg, correlationData);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String errorMsg) {
        System.out.println("callbackSender.confirm");
    }
}
