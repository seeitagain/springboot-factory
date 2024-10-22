package org.example.lc.service.mq;

import lombok.extern.slf4j.Slf4j;
import org.example.lc.pojo.domain.QuitResource;
import org.example.lc.pojo.domain.RecoveryResource;
import org.example.lc.service.MainService;
import org.example.lc.utlis.JsonUtil;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Slf4j
@Component
@EnableRetry
public class QuitReceiver {
    @Autowired
    private MainService mainService;

    @Value("${queue.name.quit}")
    private String queueName;

    @Retryable(
            value = Exception.class,
            maxAttempts = 3, // 设置最大重试次数
            backoff = @Backoff(delay = 2000)
    )
    @RabbitListener(queues = "#{@quitQueueName}")
    public void process(String msg) throws Exception{
        log.info("{} receive msg:{}", queueName, msg);
        QuitResource resource = JsonUtil.deserialize(msg, QuitResource.class);
        if (resource == null || CollectionUtils.isEmpty(resource.getSystemCodes())) {
            log.error("报文有误");
            throw new Exception("模拟异常");
        }
    }

    @Recover
    public void recover(Exception e, String message) {
        // 这里处理超过最大重试次数的消息
        System.out.println("Message exceeded max retry attempts: " + message);
//        saveMessageToDatabase(message);
    }
}
