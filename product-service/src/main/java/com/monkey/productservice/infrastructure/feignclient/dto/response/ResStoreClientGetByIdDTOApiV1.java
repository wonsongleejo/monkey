package com.monkey.productservice.infrastructure.feignclient.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResStoreClientGetByIdDTOApiV1 {
    private UUID storeId;
    private String storeName;
}
