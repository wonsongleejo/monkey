package com.monkey.productreservationservice.domain.vo;

public enum ProductReservationStatus {
    PENDING_PICKUP,
    PICKED_UP,
    CANCELED,
    FAILED,
    WAITING_FOR_CONFIRM // 예약 요청 받은 후 확정되지 않은 상태
}