package com.monkey.storeservice.application.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReqStorePostDtoApiV1 {

  @NotBlank(message = "팝업스토어 이름을 입력해주세요.")
  private String storeName;

  @NotBlank(message = "팝업스토어 설명을 입력해주세요.")
  private String description;

  @NotNull(message = "팝업스토어 상태를 입력해주세요.")
  private OpenStatus openStatus;

  @NotNull(message = "팝업스토어 오픈날짜를 입력해주세요.")
  private LocalDate startDate;

  @NotNull(message = "팝업스토어 종료날짜를 입력해주세요.")
  private LocalDate endDate;

  @NotNull(message = "팝업스토어 시작시간을 입력해주세요.")
  private LocalTime startTime;

  @NotNull(message = "팝업스토어 종료시간을 입력해주세요.")
  private LocalTime endTime;

  @NotNull(message = "팝업스토어 총 수용 인원을 입력해주세요.")
  @Min(1)
  private Integer totalPerson;

  public enum OpenStatus {
    OPEN, CLOSE;

  }
}