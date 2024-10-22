package org.example.lc.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.lc.pojo.domain.ApplyResource;
import org.example.lc.pojo.domain.QuitResource;
import org.example.lc.pojo.domain.RecoveryResource;
import org.example.lc.service.mq.Sender;
import org.example.lc.service.onesendmultirecver.HelloOneSender;
import org.example.lc.utlis.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created with IDEA
 * User: vector
 * Data: 2017/7/7
 * Time: 17:06
 * Description:
 */
@RestController
@RequestMapping("/rabbit2")
@Api(value = "MqController", tags = "模拟发送mq消息")
public class MqController {
    @Autowired
    private HelloOneSender helloSender1;

    @Autowired
    private Sender sender;

    @Value("${queue.name.apply}")
    private String applyQueueName;

    @Value("${queue.name.recovery}")
    private String recoveryQueueName;

    @Value("${queue.name.quit}")
    private String quitQueueName;

    @ApiOperation("单生产者-多消费者")
    @RequestMapping(value = "/oneToMany", method = RequestMethod.GET)
    public void oneToMany() {
        for (int i = 0; i < 10; i++) {
            helloSender1.send("hellomsg:" + i);
        }
    }

    /**
     * 含义为：只是给员工授予git、jenkins的登录权限，不包含git、jenkins里面的仓库权限、项目权限等
     * 在ldap中新增用户，用于登录git、jenkins时进行验证
     * 记录操作流水
     * @param resource
     * @return
     */
    @ApiOperation("员工资源申请流程")
    @PostMapping("/apply")
    public Boolean send(@Valid @RequestBody ApplyResource resource) {
        sender.send(applyQueueName, JsonUtil.serialize(resource));
        return Boolean.TRUE;
    }

    @ApiOperation("资源回收流程")
    @PostMapping("/recovery")
    public Boolean recovery(@Valid @RequestBody RecoveryResource resource) {
        sender.send(recoveryQueueName, JsonUtil.serialize(resource));
        return Boolean.TRUE;
    }

    /**
     * 删除ldap账号信息
     * git用户信息置为失效
     * jenkins用户置为失效
     * 记录操作流水
     * @param resource
     * @return
     */
    @ApiOperation("员工离职资源回收流程")
    @PostMapping("/quit")
    public Boolean quit(@Valid @RequestBody QuitResource resource) {
        sender.send(quitQueueName, JsonUtil.serialize(resource));
        return Boolean.TRUE;
    }

    @ApiOperation("callback发送")
    @PostMapping("/callback")
    public void callback(@Valid @RequestBody QuitResource resource) {
        sender.send2(quitQueueName, JsonUtil.serialize(resource));
    }
}
