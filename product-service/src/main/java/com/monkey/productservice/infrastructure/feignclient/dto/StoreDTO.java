package com.monkey.productservice.infrastructure.feignclient.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreDTO {
    private UUID storeId;
    private String storeName;
}
