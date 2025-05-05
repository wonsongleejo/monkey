package com.monkey.productservice.application.event.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class ProductStockIncreaseFailPayloadV1 {
    private UUID productId;
}
