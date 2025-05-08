package com.monkey.storereservationservice.domain.vo;

public enum StoreReservationStatus {
    SCHEDULED,
    VISITED,
    CANCELED,
    NO_SHOW,
    SCHEDULED_PENDING,
    CANCELED_PENDING,
    SCHEDULED_FAILED,
    CANCELED_FAILED
}