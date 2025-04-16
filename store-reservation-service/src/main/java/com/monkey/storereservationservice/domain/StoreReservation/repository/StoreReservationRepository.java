package com.monkey.storereservationservice.domain.storereservation.repository;

import com.monkey.storereservationservice.domain.storereservation.entity.StoreReservationEntity;

import java.util.UUID;

public interface StoreReservationRepository {
    StoreReservationEntity save(StoreReservationEntity storeReservationEntity);
    StoreReservationEntity findById(UUID storeReservationId);
    long count();
}