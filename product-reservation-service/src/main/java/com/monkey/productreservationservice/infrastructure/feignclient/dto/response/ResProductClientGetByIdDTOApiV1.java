package com.monkey.productreservationservice.infrastructure.feignclient.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResProductClientGetByIdDTOApiV1 {
    private UUID productId;
    private UUID storeId;
    private String productName;
    private Integer quantity;
    private Integer purchaseLimitPerUser;
}
