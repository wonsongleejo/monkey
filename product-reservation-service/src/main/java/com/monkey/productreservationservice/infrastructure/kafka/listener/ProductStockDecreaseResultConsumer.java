package com.monkey.productreservationservice.infrastructure.kafka.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monkey.productreservationservice.domain.entity.ProductReservationEntity;
import com.monkey.productreservationservice.domain.repository.ProductReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductStockDecreaseResultConsumer {

    private final ProductReservationRepository productReservationRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "product.product-reservation.stock-decrease-success", groupId = "product-reservation-service-group")
    public void consumeStockDecreaseSuccess(String message) {
        try {
            log.info("[Kafka] 재고 차감 성공 이벤트 수신: {}", message);
            UUID reservationId = extractReservationId(message);
            Optional<ProductReservationEntity> optional = productReservationRepository.findByProductReservationIdAndIsDeletedFalse(reservationId);
            if (optional.isPresent()) {
                ProductReservationEntity reservation = optional.get();
                reservation.confirmStockReservation();
                productReservationRepository.save(reservation);
            }
        } catch (Exception e) {
            log.error("[Kafka] 재고 차감 성공 처리 실패", e);
        }
    }

    @KafkaListener(topics = "product.product-reservation.stock-decrease-fail", groupId = "product-reservation-service-group")
    public void consumeStockDecreaseFail(String message) {
        try {
            log.info("[Kafka] 재고 차감 실패 이벤트 수신: {}", message);
            UUID reservationId = extractReservationId(message);
            Optional<ProductReservationEntity> optional = productReservationRepository.findByProductReservationIdAndIsDeletedFalse(reservationId);
            if (optional.isPresent()) {
                ProductReservationEntity reservation = optional.get();
                reservation.failStockReservation();
                productReservationRepository.save(reservation);
            }
        } catch (Exception e) {
            log.error("[Kafka] 재고 차감 실패 처리 실패", e);
        }
    }

    private UUID extractReservationId(String json) throws Exception {
        return objectMapper.readTree(json).get("productReservationId").traverse().readValueAs(UUID.class);
    }
}
