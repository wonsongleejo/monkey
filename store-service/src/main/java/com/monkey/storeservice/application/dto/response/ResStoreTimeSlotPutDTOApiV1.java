package com.monkey.storeservice.application.dto.response;

import com.monkey.storeservice.domain.entity.StoreTimeSlotEntity;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResStoreTimeSlotPutDTOApiV1 {

  private LocalDate slotDate;

  private LocalTime entryTime;

  private LocalTime exitTime;

  private Integer maxPerson;

  public static ResStoreTimeSlotPutDTOApiV1 of(StoreTimeSlotEntity storeTimeSlotEntity) {
    return ResStoreTimeSlotPutDTOApiV1.builder()
        .slotDate(storeTimeSlotEntity.getSlotDate())
        .entryTime(storeTimeSlotEntity.getEntryTime())
        .exitTime(storeTimeSlotEntity.getExitTime())
        .maxPerson(storeTimeSlotEntity.getMaxPerson())
        .build();
  }
}
