package com.monkey.slackservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "com.monkey")
@EnableFeignClients
public class SlackServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SlackServiceApplication.class, args);
    }

}
