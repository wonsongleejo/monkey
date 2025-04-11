package com.monkey.storeservice.application.dto.response;

import com.monkey.storeservice.domain.article.entity.StoreTimeSlotEntity;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResStoreTimeSlotPutDtoApiV1 {

  private LocalDate slotDate;

  private LocalTime entryTime;

  private LocalTime exitTime;

  private Integer maxPerson;

  public static ResStoreTimeSlotPutDtoApiV1 of(StoreTimeSlotEntity storeTimeSlotEntity) {
    return ResStoreTimeSlotPutDtoApiV1.builder()
        .slotDate(storeTimeSlotEntity.getSlotDate())
        .entryTime(storeTimeSlotEntity.getEntryTime())
        .exitTime(storeTimeSlotEntity.getExitTime())
        .maxPerson(storeTimeSlotEntity.getMaxPerson())
        .build();
  }
}
