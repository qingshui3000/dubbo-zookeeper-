package com.stylefeng.guns.rest;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = {"com.stylefeng.guns"})
@EnableDubbo
@EnableAsync
public class GunsGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GunsGatewayApplication.class, args);
    }
}
