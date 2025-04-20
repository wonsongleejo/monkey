package com.monkey.storeservice.application.dto.response;

import com.monkey.storeservice.domain.entity.StoreEntity;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResStorePutDTOApiV1 {

  private UUID storeId;
  private String storeName;
  private String description;
  private StoreEntity.OpenStatus openStatus;
  private LocalDate startDate;
  private LocalDate endDate;
  private LocalTime startTime;
  private LocalTime endTime;
  private Integer totalPersonCount;

  public static ResStorePutDTOApiV1 of(StoreEntity storeEntity) {
    return ResStorePutDTOApiV1.builder()
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
}