package com.monkey.storeservice.application.dto.response;

import com.monkey.storeservice.application.dto.request.ReqStoreTimeSlotPostDtoApiV1.StoreTimeSlot;
import com.monkey.storeservice.domain.article.entity.StoreTimeSlotEntity;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResStoreTimeSlotGetDtoApiV1 {

  private StoreTimeSlot storeTimeSlot;
  private List<StoreTimeSlot> storeTimeSlotsList;

  public static ResStoreTimeSlotGetDtoApiV1 of(StoreTimeSlotEntity storeTimeSlotEntity) {
    return ResStoreTimeSlotGetDtoApiV1.builder()
        .storeTimeSlot(StoreTimeSlot.from(storeTimeSlotEntity))
        .build();
  }
  public static ResStoreTimeSlotGetDtoApiV1 of(List<StoreTimeSlotEntity> storeTimeSlotEntity) {
    return ResStoreTimeSlotGetDtoApiV1.builder()
        .storeTimeSlotsList(StoreTimeSlot.from(storeTimeSlotEntity))
        .build();
  }

  @Getter
  @Builder
  public static class StoreTimeSlot{

    private UUID storeId;

    private UUID slotId;

    private LocalDate slotDate;

    private LocalTime entryTime;

    private LocalTime exitTime;

    private Integer maxPerson;

    public static StoreTimeSlot from(StoreTimeSlotEntity storeTimeSlotEntity) {
      return StoreTimeSlot.builder()
          .storeId(storeTimeSlotEntity.getStoreId())
          .slotId(storeTimeSlotEntity.getTimeslotId())
          .slotDate(storeTimeSlotEntity.getSlotDate())
          .entryTime(storeTimeSlotEntity.getEntryTime())
          .exitTime(storeTimeSlotEntity.getExitTime())
          .maxPerson(storeTimeSlotEntity.getMaxPerson())
          .build();
    }
    //전체조회
    public static List<StoreTimeSlot> from(List<StoreTimeSlotEntity> storeTimeSlotEntityList){
      return storeTimeSlotEntityList.stream()
          .map(storeTimeSlotEntity -> StoreTimeSlot.from(storeTimeSlotEntity))
          .toList();
    }
  }
}
