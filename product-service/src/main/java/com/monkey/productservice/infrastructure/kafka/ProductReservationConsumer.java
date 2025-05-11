package com.monkey.productservice.infrastructure.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monkey.productservice.application.event.dto.ProductStockDecreaseResultPayloadV1;
import com.monkey.productservice.application.service.v4.ProductServiceApiV4;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductReservationConsumer {
    private final ProductServiceApiV4 productService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String TOPIC_SUCCESS = "product.product-reservation.stock-decrease-success";
    private static final String TOPIC_FAIL = "product.product-reservation.stock-decrease-fail";

    @KafkaListener(topics = "product-reservation.created", groupId = "product-service-group")
    public void consumeReservationCreated(String message) {
        log.info("[Kafka] 상품예약 생성 메시지 수신: {}", message);

        try {
            String[] parts = message.split(",");
            UUID reservationId = UUID.fromString(parts[0].split("=")[1]);
            Long userId = Long.parseLong(parts[1].split("=")[1]);
            UUID productId = UUID.fromString(parts[2].split("=")[1]);
            int quantity = Integer.parseInt(parts[3].split("=")[1]);

            productService.decreaseStock(productId, userId, quantity);
            log.info("[Kafka] 재고 차감 완료: productId={}, quantity={}", productId, quantity);

            ProductStockDecreaseResultPayloadV1 payload = ProductStockDecreaseResultPayloadV1.builder()
                    .productReservationId(reservationId)
                    .status("SUCCESS")
                    .build();
            kafkaTemplate.send(TOPIC_SUCCESS, objectMapper.writeValueAsString(payload));

        } catch (Exception e) {
            log.error("[Kafka] 재고 차감 실패", e);
            try {
                UUID reservationId = UUID.fromString(message.split(",")[0].split("=")[1]);
                ProductStockDecreaseResultPayloadV1 payload = ProductStockDecreaseResultPayloadV1.builder()
                        .productReservationId(reservationId)
                        .status("FAIL")
                        .build();
                kafkaTemplate.send(TOPIC_FAIL, objectMapper.writeValueAsString(payload));
            } catch (Exception ex) {
                log.error("[Kafka] 실패 응답 메시지 생성 중 에러", ex);
            }
        }
    }
}
