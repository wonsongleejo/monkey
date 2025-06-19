package com.monkey.productreservationservice.infrastructure.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProductReservationProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "product-reservation.created";
    // 상품 서비스의 ProductReservationConsumer가 위 토픽 수신

    public void sendReservationCreated(String message) {
        log.info("[Kafka] 상품 예약 이벤트 발행: {}", message);
        kafkaTemplate.send(TOPIC, message)
                .thenAccept(result -> log.info("[Kafka] 메시지 전송 성공: {}", result))
                .exceptionally(ex -> {
                    log.error("[Kafka] 메시지 전송 실패", ex);
                    return null;
                });
    }
}
