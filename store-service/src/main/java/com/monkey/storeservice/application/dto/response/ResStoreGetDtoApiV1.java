package com.monkey.storeservice.application.dto.response;

import com.monkey.storeservice.domain.article.entity.StoreEntity;
import java.util.List;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResStoreGetDtoApiV1 {

  private Store store;
  private List<Store> storeList;

  public static ResStoreGetDtoApiV1 of(StoreEntity storeEntity) {
    return ResStoreGetDtoApiV1.builder()
        .store(Store.from(storeEntity))
        .build();
  }
  public static ResStoreGetDtoApiV1 of(List<StoreEntity> storeEntityList) {
    return ResStoreGetDtoApiV1.builder()
        .storeList(Store.from(storeEntityList))
        .build();
  }

  @Getter
  @Builder
  public static class Store {

    private UUID storeId;
    private String storeName;
    private String description;
    private StoreEntity.OpenStatus openStatus;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer totalPersonCount;

    public static Store from(StoreEntity storeEntity) {
      return Store.builder()
          .storeId(storeEntity.getStoreId())
          .storeName(storeEntity.getStoreName())
          .description(storeEntity.getDescription())
          .openStatus(storeEntity.getOpenStatus())
          .startDate(storeEntity.getStartDate())
          .endDate(storeEntity.getEndDate())
          .startTime(storeEntity.getStartTime())
          .endTime(storeEntity.getEndTime())
          .totalPersonCount(storeEntity.getTotalPersonCount())
          .build();
    }

    public static List<Store> from(List<StoreEntity> storeEntityList) {
      return storeEntityList.stream()
          .map(storeEntity -> Store.from(storeEntity))
          .toList();

    }
  }
}