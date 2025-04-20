package com.monkey.storeservice.application.dto.request;

import com.monkey.storeservice.domain.entity.StoreTimeSlotEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReqStoreTimeSlotPostDTOApiV1 {

  @Valid
  @NotNull
  private StoreTimeSlot storeTimeSlot;

  @Getter
  @Builder
  public static class StoreTimeSlot {

    @NotNull(message = "예약 날짜를 입력해주세요.")
    private LocalDate slotDate;

    @NotNull(message = "입장 시간을 입력해주세요.")
    private LocalTime entryTime;

    @NotNull(message = "퇴장 시간을 입력해주세요.")
    private LocalTime exitTime;

    @NotNull(message = "최대 인원을 입력해주세요.")
    private Integer maxPerson;

    public StoreTimeSlotEntity toStoreTimeSlotEntity() {

      return StoreTimeSlotEntity.builder()
          .slotDate(slotDate)
          .entryTime(entryTime)
          .exitTime(exitTime)
          .maxPerson(maxPerson)
          .build();
    }
  }
}
