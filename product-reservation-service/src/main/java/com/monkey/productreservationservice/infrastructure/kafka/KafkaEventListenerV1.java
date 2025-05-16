package com.monkey.productreservationservice.infrastructure.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monkey.productreservationservice.application.service.ProductReservationServiceApiV3;
import com.monkey.productreservationservice.infrastructure.kafka.dto.ProductStockIncreaseFailPayloadV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaEventListenerV1 {
    private final ProductReservationServiceApiV3 productReservationService;

    @KafkaListener(groupId = "reservation-cancel-failed", topics = "product.product-reservation.reservation-cancel-failed")
    public void increaseStockFailed(
            @Payload String payload,
            @Header(value = "X-User-Id", required = false) String userId
    ) {
        try {
            log.info("receive cancel failed event sent from product");
            ObjectMapper objectMapper = new ObjectMapper();
            ProductStockIncreaseFailPayloadV1 productStockIncreaseFailPayload = objectMapper.readValue(payload, ProductStockIncreaseFailPayloadV1.class);
            productReservationService.cancelFailed(productStockIncreaseFailPayload.getProductId(), Long.valueOf(userId));
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
