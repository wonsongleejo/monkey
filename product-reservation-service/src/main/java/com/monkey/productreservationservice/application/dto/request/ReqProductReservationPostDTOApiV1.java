package com.monkey.productreservationservice.application.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReqProductReservationPostDTOApiV1 {
    @NotNull(message = "구매할 수량을 입력하세요.")
    @Min(value = 1, message = "구매 수량은 1개 이상이어야 합니다.")
    private Integer quantity;
}