package com.monkey.storereservationservice.domain.StoreReservation.repository;

import com.monkey.storereservationservice.domain.StoreReservation.entity.StoreReservationEntity;

import java.util.UUID;

public interface StoreReservationRepository {

    StoreReservationEntity save(StoreReservationEntity storeReservationEntity);

    StoreReservationEntity findById(UUID storeReservationId);
}