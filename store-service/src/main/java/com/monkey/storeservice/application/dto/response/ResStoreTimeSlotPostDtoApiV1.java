package com.monkey.storeservice.application.dto.response;

import com.monkey.storeservice.domain.article.entity.StoreTimeSlotEntity;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResStoreTimeSlotPostDtoApiV1 {

  private StoreTimeSlot storeTimeSlot;

  public static ResStoreTimeSlotPostDtoApiV1 of(StoreTimeSlotEntity storeTimeSlotEntity) {
    return ResStoreTimeSlotPostDtoApiV1.builder()
        .storeTimeSlot(StoreTimeSlot.from(storeTimeSlotEntity))
        .build();
  }

  @Getter
  @Builder
  public static class StoreTimeSlot {

    private UUID timeSlotId;

    public static StoreTimeSlot from(StoreTimeSlotEntity storeTimeSlotEntity) {
      return StoreTimeSlot.builder()
          .timeSlotId(storeTimeSlotEntity.getTimeSlotId())
          .build();
    }

  }

}
