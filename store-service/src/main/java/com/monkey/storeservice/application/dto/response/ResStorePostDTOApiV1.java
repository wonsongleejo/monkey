package com.monkey.storeservice.application.dto.response;


import com.monkey.storeservice.domain.entity.StoreEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResStorePostDTOApiV1 {

  private Store store;

  public static ResStorePostDTOApiV1 of(StoreEntity storeEntity) {
    return ResStorePostDTOApiV1.builder()
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