package com.monkey.commonmodule.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ResponseCode {

    // 공통
    SUCCESS("000", "성공적으로 처리됐습니다.", HttpStatus.OK),
    NEED_LOGIN("001", "로그인이 필요합니다.", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("002", "권한이 없습니다.", HttpStatus.FORBIDDEN),
    DUPLICATED_REQUEST("003", "중복된 요청입니다.", HttpStatus.CONFLICT),
    NOT_FOUND("004", "존재하지 않는 데이터입니다.", HttpStatus.NOT_FOUND),

    // User
    USER_ALREADY_EXISTS("US100", "이미 등록된 회원입니다.", HttpStatus.CONFLICT),
    USER_NOT_FOUND("US200", "존재하지 않는 회원입니다.", HttpStatus.NOT_FOUND),
    PASSWORD_MISMATCH("US201", "비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),

    // Store
    STORE_ALREADY_EXISTS("S100", "이미 등록된 스토어입니다.", HttpStatus.CONFLICT),
    STORE_TIME_DUPLICATE("S101", "이미 등록된 시간대입니다.", HttpStatus.CONFLICT),

    // Store-Reservation
    STORE_RESERVATION_FULL("SR100", "예약이 마감되었습니다.", HttpStatus.BAD_REQUEST),
    OUT_OF_RESERVATION_TIME("SR101", "예약 가능한 시간에서 벗어났습니다.", HttpStatus.BAD_REQUEST),

    // Product
    PRODUCT_ALREADY_EXISTS("P100", "이미 등록된 상품입니다.", HttpStatus.CONFLICT),

    // Product-Reservation
    PRODUCT_RESERVATION_FULL("PR100", "예약이 마감되었습니다.", HttpStatus.BAD_REQUEST),
    NOT_STORE_MEMBER("PR101", "스토어 예약 회원이 아닙니다.", HttpStatus.FORBIDDEN),

    // Slack
    SLACK_SEND_FAILED("SL100", "전송이 실패되었습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    // 예상치 못한 예외 발생
    INTERNAL_SERVER_ERROR("999", "서버 에러가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus status;

    ResponseCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}

