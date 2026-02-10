package io.github.seeitagain;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "io.github.seeitagain.mybatis.mapper")
public class MybatisPlusApplication {
    public static void main(String[] args) {

        // 程序启动入口
        // 启动嵌入式的 Tomcat 并初始化 Spring 环境及其各 Spring 组件
        SpringApplication.run(MybatisPlusApplication.class, args);
    }
}
