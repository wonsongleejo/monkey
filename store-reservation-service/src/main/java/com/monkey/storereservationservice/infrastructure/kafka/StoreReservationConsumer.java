package com.monkey.storereservationservice.infrastructure.kafka;

import com.monkey.storereservationservice.domain.entity.StoreReservationEntity;
import com.monkey.storereservationservice.domain.repository.StoreReservationRepository;
import com.monkey.storereservationservice.domain.vo.StoreReservationStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class StoreReservationConsumer {

    private final StoreReservationRepository storeReservationRepository;

    @KafkaListener(topics = "store-reservation-created", groupId = "store-reservation-group")
    public void listenReservationCreated(String message) {
        log.info("[Kafka] 예약 생성 메시지 수신: {}", message);

        try {
            String[] parts = message.split(",");
            String reservationIdStr = parts[0].split("=")[1];
            String status = parts[4].split("=")[1];

            // PENDING 상태면 저장하지 않고 소비만
            if (status.equals("SCHEDULED_PENDING")) {
                log.info("[Kafka] SCHEDULED_PENDING 상태는 소비만");
                return;
            }

            UUID reservationId = UUID.fromString(reservationIdStr);
            StoreReservationEntity entity = storeReservationRepository.findById(reservationId);

            if (entity == null) {
                log.warn("[Kafka] 존재하지 않는 예약 ID: {}", reservationId);
                return;
            }

            try {
                StoreReservationStatus parsedStatus = StoreReservationStatus.valueOf(status);
                entity.changeStatus(parsedStatus);
                storeReservationRepository.save(entity);
                log.info("[Kafka] 상태 변경 완료: {}", parsedStatus);
            } catch (IllegalArgumentException e) {
                log.warn("[Kafka] 지원하지 않는 상태: {}", status);
            }

        } catch (Exception e) {
            log.error("[Kafka] 예약 생성 메시지 처리 중 에러 발생", e);
        }
    }
}