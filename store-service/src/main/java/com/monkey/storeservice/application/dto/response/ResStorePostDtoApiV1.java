package com.monkey.storeservice.application.dto.response;


import com.monkey.storeservice.domain.article.entity.StoreEntity;
import java.util.List;
import java.util.stream.Collectors;
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

  private Store store;

  public static ResStorePostDtoApiV1 of(StoreEntity storeEntity) {
    return ResStorePostDtoApiV1.builder()
        .store(Store.from(storeEntity))
        .build();
  }

  @Getter
  @Builder
  public static class Store{

    private UUID storeId;

    public static Store from(StoreEntity storeEntity) {
      return Store.builder()
          .storeId(storeEntity.getStoreId())
          .build();
    }
  }
}