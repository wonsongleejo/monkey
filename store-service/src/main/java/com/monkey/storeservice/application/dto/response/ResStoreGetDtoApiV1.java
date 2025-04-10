package com.monkey.storeservice.application.dto.response;

import com.monkey.storeservice.domain.article.entity.StoreEntity;
import java.util.List;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import org.h2.mvstore.db.Store;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResStoreGetDtoApiV1 {

  private UUID storeId;
  private String storeName;
  private String description;
  private StoreEntity.OpenStatus openStatus;
  private LocalDate startDate;
  private LocalDate endDate;
  private LocalTime startTime;
  private LocalTime endTime;
  private Integer totalPerson;

  public static ResStoreGetDtoApiV1 of(StoreEntity storeEntity) {
    return ResStoreGetDtoApiV1.builder()
        .storeId(storeEntity.getStoreId())
        .storeName(storeEntity.getStoreName())
        .description(storeEntity.getDescription())
        .openStatus(storeEntity.getOpenStatus())
        .startDate(storeEntity.getStartDate())
        .endDate(storeEntity.getEndDate())
        .startTime(storeEntity.getStartTime())
        .endTime(storeEntity.getEndTime())
        .totalPerson(storeEntity.getTotalPerson())
        .build();
  }
  public static List<ResStoreGetDtoApiV1> of(List<StoreEntity> storeList) {
    return storeList.stream()
        .map(ResStoreGetDtoApiV1::of)
        .toList();
  }
}