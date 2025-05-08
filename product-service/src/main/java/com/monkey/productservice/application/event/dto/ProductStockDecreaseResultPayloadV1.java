package com.monkey.productservice.application.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductStockDecreaseResultPayloadV1 {
    private UUID productReservationId;
    private String status; // SUCCESS or FAIL
}
