package com.monkey.productreservationservice.infrastructure.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Bean
    public NewTopic reservationCancelSuccessTopic() {
        // 토픽명 : <프로듀서명>.<컨슈머명>.<이벤트-상태>
        return new NewTopic("product-reservation.product.reservation-cancel-success", 1, (short) 1);
    }
}
