package com.monkey.productservice.infrastructure.feignclient.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResUserClientGetByIdDTOApiV1 {
    private Long userId;
}
