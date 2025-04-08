package com.monkey.commonmodule.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ResDTO<T> {
    private String code;
    private String message;
    private T data;

    // 성공 시 응답
    public static <T> ResDTO<T> success(T data) {
        return ResDTO.<T>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
                .data(data)
                .build();
    }

    // 실패 시 응답
    public static <T> ResDTO<T> fail(ResponseCode code) {
        return ResDTO.<T>builder()
                .code(code.getCode())
                .message(code.getMessage())
                .data(null)
                .build();
    }
}
