package com.monkey.storeservice.application.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResStorePostDtoApiV1 {
  private UUID storeId;

  public static ResStorePostDtoApiV1 of(UUID storeId) {
    return ResStorePostDtoApiV1.builder()
        .storeId(storeId)
        .build();
  }
}