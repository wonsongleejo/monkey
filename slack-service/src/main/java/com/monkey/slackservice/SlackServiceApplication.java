package com.monkey.slackservice;

import com.monkey.slackservice.infrastructure.config.SlackProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "com.monkey")
@EnableFeignClients
@EnableConfigurationProperties(SlackProperties.class)
public class SlackServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SlackServiceApplication.class, args);
    }

}
