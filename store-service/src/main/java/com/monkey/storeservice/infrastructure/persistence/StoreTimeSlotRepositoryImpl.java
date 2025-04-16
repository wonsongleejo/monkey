package com.monkey.storeservice.infrastructure.persistence;

import com.monkey.storeservice.domain.article.entity.StoreTimeSlotEntity;
import com.monkey.storeservice.domain.article.repository.StoreTimeSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class StoreTimeSlotRepositoryImpl implements StoreTimeSlotRepository {

  private final StoreTimeSlotJpaRepository jpaRepository;
  private final StoreTimeSlotJpaRepository storeTimeSlotJpaRepository;

  @Override
  public StoreTimeSlotEntity save(StoreTimeSlotEntity entity) {
    return jpaRepository.save(entity);
  }

  @Override
  public Optional<StoreTimeSlotEntity> findById(UUID timeSlotId) {
    return jpaRepository.findById(timeSlotId);
  }

  @Override
  public List<StoreTimeSlotEntity> findAll() {
    return jpaRepository.findAll();
  }

  @Override
  public long count() {
    return storeTimeSlotJpaRepository.count();
  }
}