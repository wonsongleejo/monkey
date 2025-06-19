package com.monkey.productservice.application.event.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class ProductStockIncreasePayloadV1 {
    private UUID productId;
    private int quantity;
}
