package com.monkey.storeservice.infrastructure.persistence;

import com.monkey.storeservice.domain.entity.StoreTimeSlotEntity;
import com.monkey.storeservice.domain.repository.StoreTimeSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

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
  public Page<StoreTimeSlotEntity> findAll(Pageable pageable) {
    return jpaRepository.findAll(pageable);
  }

  @Override
  public long count() {

    return storeTimeSlotJpaRepository.count();
  }
}