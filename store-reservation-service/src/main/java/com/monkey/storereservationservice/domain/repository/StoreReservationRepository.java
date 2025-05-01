package com.monkey.storereservationservice.domain.repository;

import com.monkey.storereservationservice.domain.entity.StoreReservationEntity;

import java.util.List;
import java.util.UUID;

public interface StoreReservationRepository {

    StoreReservationEntity save(StoreReservationEntity storeReservationEntity);

    Integer sumPersonCountByTimeSlotId(UUID timeSlotId);

    List<StoreReservationEntity> findAll();

    StoreReservationEntity findById(UUID storeReservationId);

    long count();
}