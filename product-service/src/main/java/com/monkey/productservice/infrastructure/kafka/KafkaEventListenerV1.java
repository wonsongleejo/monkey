package com.monkey.productservice.infrastructure.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monkey.productservice.application.service.v3.ProductServiceApiV3;
import com.monkey.productservice.infrastructure.kafka.dto.ProductStockIncreasePayloadV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaEventListenerV1 {
    private final ProductServiceApiV3 productService;

    @KafkaListener(groupId = "reservation-cancel", topics = "product-reservation.product.reservation-cancel-success")
    public void increaseStock(@Payload String payload) {
        try {
            log.info("receive reservation event sent from product-reservation");
            ObjectMapper objectMapper = new ObjectMapper();
            ProductStockIncreasePayloadV1 productStockIncreasePayload = objectMapper.readValue(payload, ProductStockIncreasePayloadV1.class);
            productService.increaseStock(productStockIncreasePayload.getProductId(), productStockIncreasePayload.getQuantity());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}