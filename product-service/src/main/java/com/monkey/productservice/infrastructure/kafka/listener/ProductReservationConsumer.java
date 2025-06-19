package com.monkey.productservice.infrastructure.kafka.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monkey.productservice.application.event.dto.ProductStockDecreaseResultPayloadV1;
import com.monkey.productservice.application.service.ProductServiceApiV4;
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
            // JSON 직접 파싱
            var root = objectMapper.readTree(message);
            UUID productReservationId = UUID.fromString(root.get("productReservationId").asText());
            UUID productId = UUID.fromString(root.get("productId").asText());
            int quantity = root.get("quantity").asInt();

            productService.decreaseStock(productId, quantity);
            log.info("[Kafka] 재고 차감 완료: productId={}, quantity={}", productId, quantity);

            // 재고 차감 성공 이벤트 발행
            ProductStockDecreaseResultPayloadV1 successPayload = ProductStockDecreaseResultPayloadV1.builder()
                    .productReservationId(productReservationId)
                    .status("SUCCESS")
                    .build();
            kafkaTemplate.send(TOPIC_SUCCESS, objectMapper.writeValueAsString(successPayload));

        } catch (Exception e) {
            log.error("[Kafka] 재고 차감 실패", e);
            try {
                UUID reservationId = UUID.fromString(
                        objectMapper.readTree(message).get("productReservationId").asText()
                );
                ProductStockDecreaseResultPayloadV1 failPayload = ProductStockDecreaseResultPayloadV1.builder()
                        .productReservationId(reservationId)
                        .status("FAIL")
                        .build();
                kafkaTemplate.send(TOPIC_FAIL, objectMapper.writeValueAsString(failPayload));
            } catch (Exception ex) {
                log.error("[Kafka] 실패 응답 메시지 생성 중 에러", ex);
            }
        }
    }
}
