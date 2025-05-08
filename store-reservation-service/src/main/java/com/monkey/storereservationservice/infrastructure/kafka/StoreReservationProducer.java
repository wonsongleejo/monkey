package com.monkey.storereservationservice.infrastructure.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class StoreReservationProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final String RESERVATION_TOPIC = "store-reservation-created";

    public void sendReservationCreated(String message) {
        log.info("[Kafka] 예약 이벤트 발행: {}", message);
        kafkaTemplate.send(RESERVATION_TOPIC, message)
                .thenAccept(result -> log.info("[Kafka] 메시지 전송 성공: {}", result))
                .exceptionally(ex -> {
                    log.error("[Kafka] 메시지 전송 실패", ex);
                    return null;
                });
    }
}