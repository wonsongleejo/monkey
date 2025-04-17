package com.monkey.storeservice.domain.article.repository;

import com.monkey.storeservice.domain.article.entity.StoreTimeSlotEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StoreTimeSlotRepository {

  StoreTimeSlotEntity save(StoreTimeSlotEntity entity);

  Optional<StoreTimeSlotEntity> findById(UUID timeSlotId);

  List<StoreTimeSlotEntity> findAll();

  long count();
}