package org.example.lc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableSwagger2
public class JdbcApplication {
//    @Bean
//    public Docket createRestApi() {
//        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
//                .apis(RequestHandlerSelectors.basePackage("org.example.lc")) //扫描API的包路径
//                .paths(PathSelectors.any()).build();
//    }
//
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder().title("SpringBoot整合Knife") // 标题
//                .description("api接口的文档整理，支持在线测试") // 描述
//                .contact(new Contact("vector.wang", "http://blog.wangxc.club/", "vector4wang@qq.com"))
//                .version("1.0") // 版本号
//                .build();
//    }
    public static void main(String[] args) {

        // 程序启动入口
        // 启动嵌入式的 Tomcat 并初始化 Spring 环境及其各 Spring 组件
        SpringApplication.run(JdbcApplication.class, args);
    }
}
