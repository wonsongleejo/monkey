package com.monkey.productreservationservice.application.event.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class ProductStockIncreasePayloadV1 {
    private UUID productId;
    private Integer quantity;
}
