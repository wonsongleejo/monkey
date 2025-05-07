package com.monkey.productservice.infrastructure.kafka.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class ProductStockIncreasePayloadV1 {
    private UUID productId;
    private int quantity;
}
