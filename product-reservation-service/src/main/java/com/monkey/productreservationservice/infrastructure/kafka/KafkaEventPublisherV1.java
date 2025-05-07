package com.monkey.productreservationservice.infrastructure.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monkey.productreservationservice.application.event.ProductReservationEventProduceV1;
import com.monkey.productreservationservice.application.event.dto.ProductStockIncreasePayloadV1;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaEventPublisherV1 implements ProductReservationEventProduceV1 {
    private final static String INCREASE_STOCK_TOPIC = "product-reservation.product.reservation-cancel-success";
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void increaseStock(ProductStockIncreasePayloadV1 build) {
        try {
            String payload = objectMapper.writeValueAsString(build);
            kafkaTemplate.send(INCREASE_STOCK_TOPIC, payload);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
