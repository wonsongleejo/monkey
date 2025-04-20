package com.monkey.storeservice.application.dto.request;

import com.monkey.storeservice.domain.entity.StoreEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReqStorePostDTOApiV1 {

  @Valid
  @NotNull
  private Store store;

  @Getter
  @Builder
  public static class Store{

    @NotBlank(message = "팝업스토어 이름을 입력해주세요.")
    private String storeName;

    @NotBlank(message = "팝업스토어 설명을 입력해주세요.")
    private String description;

    @NotNull(message = "팝업스토어 상태를 입력해주세요.")
    private StoreEntity.OpenStatus openStatus;

    @NotNull(message = "팝업스토어 오픈날짜를 입력해주세요.")
    private LocalDate startDate;

    @NotNull(message = "팝업스토어 종료날짜를 입력해주세요.")
    private LocalDate endDate;

    @NotNull(message = "팝업스토어 시작시간을 입력해주세요.")
    private LocalTime startTime;

    @NotNull(message = "팝업스토어 종료시간을 입력해주세요.")
    private LocalTime endTime;

    @NotNull(message = "팝업스토어 총 수용 인원을 입력해주세요.")
    @Min(value = 1, message = "최소 수용 인원은 1명 이상이어야 합니다.")
    private Integer totalPersonCount;

    public StoreEntity toEntity() {
      return StoreEntity.builder()
          .storeName(storeName)
          .description(description)
          .openStatus(openStatus)
          .startDate(startDate)
          .endDate(endDate)
          .startTime(startTime)
          .endTime(endTime)
          .totalPersonCount(totalPersonCount)
          .build();
    }
  }
}