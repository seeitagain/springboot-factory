package org.example.lc.service.mq;

import lombok.extern.slf4j.Slf4j;
import org.example.lc.pojo.domain.ApplyResource;
import org.example.lc.service.MainService;
import org.example.lc.utlis.JsonUtil;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Slf4j
@Component
public class ApplyResourceReceiver {
    @Autowired
    private MainService mainService;

    @Value("${queue.name.apply}")
    private String queueName;

    @RabbitListener(queues = "#{@applyQueueName}")
    public void process(String msg) {
        log.info("{} receive msg:{}", queueName, msg);
        ApplyResource resource = JsonUtil.deserialize(msg, ApplyResource.class);
        if (resource == null || CollectionUtils.isEmpty(resource.getSystemCodes())) {
            log.error("报文有误");
            return;
        }
        resource.getSystemCodes().stream().forEach(item -> {
            mainService.apply(item, resource.getInfo());
        });
    }
}
