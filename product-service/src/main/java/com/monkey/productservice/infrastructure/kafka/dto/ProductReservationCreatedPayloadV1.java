package com.monkey.productservice.infrastructure.kafka.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class ProductReservationCreatedPayloadV1 {
    private UUID productReservationId;
    private UUID productId;
    private int quantity;
}
