package com.monkey.storeservice.application.dto.response;

import com.monkey.storeservice.domain.entity.StoreEntity;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import org.springframework.data.domain.Page;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResStoreGetDTOApiV1 {

  private Store store;
  private Page<Store> storePage;

  public static ResStoreGetDTOApiV1 of(StoreEntity storeEntity) {
    return ResStoreGetDTOApiV1.builder()
        .store(Store.from(storeEntity))
        .build();
  }
  public static ResStoreGetDTOApiV1 of(Page<StoreEntity> storeEntityPage) {
    return ResStoreGetDTOApiV1.builder()
        .storePage(Store.from(storeEntityPage))
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

    public static Page<Store> from(Page<StoreEntity> storeEntityPage) {
      return storeEntityPage.map(Store::from);
    }
  }
}