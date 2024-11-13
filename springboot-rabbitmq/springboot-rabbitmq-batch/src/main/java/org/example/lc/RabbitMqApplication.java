package org.example.lc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync //启用spring异步能力，在其他方法上使用@Async 即异步执行
public class RabbitMqApplication {
    public static void main(String[] args) {

        // 程序启动入口
        // 启动嵌入式的 Tomcat 并初始化 Spring 环境及其各 Spring 组件
        SpringApplication.run(RabbitMqApplication.class, args);
    }
}
