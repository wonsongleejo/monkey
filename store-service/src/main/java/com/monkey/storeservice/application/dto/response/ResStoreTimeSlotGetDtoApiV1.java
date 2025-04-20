package com.monkey.storeservice.application.dto.response;

import com.monkey.storeservice.domain.entity.StoreTimeSlotEntity;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResStoreTimeSlotGetDTOApiV1 {

  private StoreTimeSlot storeTimeSlot;
  private Page<StoreTimeSlot> storeTimeSlotsPage;

  public static ResStoreTimeSlotGetDTOApiV1 of(StoreTimeSlotEntity storeTimeSlotEntity) {
    return ResStoreTimeSlotGetDTOApiV1.builder()
        .storeTimeSlot(StoreTimeSlot.from(storeTimeSlotEntity))
        .build();
  }
  public static ResStoreTimeSlotGetDTOApiV1 of(Page<StoreTimeSlotEntity> storeTimeSlotEntityPage) {
    return ResStoreTimeSlotGetDTOApiV1.builder()
        .storeTimeSlotsPage(StoreTimeSlot.from(storeTimeSlotEntityPage))
        .build();
  }

  @Getter
  @Builder
  public static class StoreTimeSlot {

    private UUID storeId;

    private UUID timeSlotId;

    private LocalDate slotDate;

    private LocalTime entryTime;

    private LocalTime exitTime;

    private Integer maxPerson;

    public static StoreTimeSlot from(StoreTimeSlotEntity storeTimeSlotEntity) {
      return StoreTimeSlot.builder()
          .storeId(storeTimeSlotEntity.getStoreId())
          .timeSlotId(storeTimeSlotEntity.getTimeSlotId())
          .slotDate(storeTimeSlotEntity.getSlotDate())
          .entryTime(storeTimeSlotEntity.getEntryTime())
          .exitTime(storeTimeSlotEntity.getExitTime())
          .maxPerson(storeTimeSlotEntity.getMaxPerson())
          .build();
    }

    //전체조회
    public static Page<StoreTimeSlot> from(Page<StoreTimeSlotEntity> storeTimeSlotEntityPage) {
      return storeTimeSlotEntityPage.map(StoreTimeSlot::from);
    }
  }
}
