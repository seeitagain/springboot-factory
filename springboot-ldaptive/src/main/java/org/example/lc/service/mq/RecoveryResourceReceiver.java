package org.example.lc.service.mq;

import lombok.extern.slf4j.Slf4j;
import org.example.lc.pojo.domain.ApplyResource;
import org.example.lc.pojo.domain.RecoveryResource;
import org.example.lc.service.MainService;
import org.example.lc.utlis.JsonUtil;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Slf4j
@Component
public class RecoveryResourceReceiver {
    @Autowired
    private MainService mainService;

    @Value("${queue.name.recovery}")
    private String queueName;

    @RabbitListener(queues = "#{@recoveryQueueName}")
    public void process(String msg) {
        log.info("{} receive msg:{}", queueName, msg);
        RecoveryResource resource = JsonUtil.deserialize(msg, RecoveryResource.class);
        if (resource == null || CollectionUtils.isEmpty(resource.getSystemCodes())) {
            log.error("报文有误");
            return;
        }
        resource.getSystemCodes().stream().forEach(item -> {
            mainService.recovery(item, resource.getInfo());
        });
    }
}
