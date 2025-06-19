package com.monkey.productreservationservice.infrastructure.kafka.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class ProductReservationCreatedPayloadV1 {
    private UUID productReservationId;
    private Long userId;
    private UUID productId;
    private int quantity;
}