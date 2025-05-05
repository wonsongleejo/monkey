package com.monkey.productservice.infrastructure.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

@Configuration
@EnableKafka
public class KafkaConfig {
    @Bean
    public NewTopic reservationCancelFailedTopic() {
        // 토픽명 : <프로듀서명>.<컨슈머명>.<이벤트-상태>
        return new NewTopic("product.product-reservation.reservation-cancel-failed", 1, (short) 1);
    }
}
