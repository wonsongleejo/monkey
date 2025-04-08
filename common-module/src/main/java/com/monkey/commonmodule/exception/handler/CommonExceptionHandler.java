package com.monkey.commonmodule.exception.handler;

import com.monkey.commonmodule.dto.ResDTO;
import com.monkey.commonmodule.dto.ResponseCode;
import com.monkey.commonmodule.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ResDTO<Object>> handleCustomException(CustomException e) {
        ResponseCode code = e.getResponseCode();

        return ResponseEntity.status(code.getStatus())
                .body(ResDTO.fail(code));
    }

    // 예상치 못한 예외 발생 시
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResDTO<Object>> handleUnhandledException(Exception e) {
        log.error("예상치 못한 에러 발생", e);
        return ResponseEntity.ok(
                ResDTO.fail(ResponseCode.INTERNAL_SERVER_ERROR)
        );
    }
}
