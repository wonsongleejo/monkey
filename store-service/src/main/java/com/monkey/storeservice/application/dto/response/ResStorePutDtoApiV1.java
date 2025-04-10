package com.monkey.storeservice.application.dto.response;

import com.monkey.storeservice.domain.article.entity.StoreEntity;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResStorePutDtoApiV1 {

  private UUID storeId;
  private String storeName;
  private String description;
  private StoreEntity.OpenStatus openStatus;
  private LocalDate startDate;
  private LocalDate endDate;
  private LocalTime startTime;
  private LocalTime endTime;
  private Integer totalPerson;

  public static ResStorePutDtoApiV1 of(StoreEntity storeEntity) {
    return ResStorePutDtoApiV1.builder()
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
}