package com.monkey.productservice.infrastructure.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monkey.productservice.application.event.ProductEventProduceV1;
import com.monkey.productservice.application.event.dto.ProductStockIncreaseFailPayloadV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventPublisherV1 implements ProductEventProduceV1 {
    private final static String INCREASE_STOCK_FAILED_TOPIC = "product.product-reservation.reservation-cancel-failed";
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void increaseStockFailed(ProductStockIncreaseFailPayloadV1 build) {
        try {
            String payload = objectMapper.writeValueAsString(build);
            kafkaTemplate.send(INCREASE_STOCK_FAILED_TOPIC, payload);
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
    }
}
