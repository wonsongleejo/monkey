package com.monkey.storeservice.domain.repository;

import com.monkey.storeservice.domain.entity.StoreTimeSlotEntity;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StoreTimeSlotRepository {

  StoreTimeSlotEntity save(StoreTimeSlotEntity entity);

  Optional<StoreTimeSlotEntity> findById(UUID timeSlotId);

  Page<StoreTimeSlotEntity> findAll(Pageable pageable);

  long count();
}