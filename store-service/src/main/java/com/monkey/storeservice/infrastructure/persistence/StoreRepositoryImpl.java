package com.monkey.storeservice.infrastructure.persistence;

import com.monkey.storeservice.domain.entity.StoreEntity;
import com.monkey.storeservice.domain.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class StoreRepositoryImpl implements StoreRepository {

  private final StoreJpaRepository storeJpaRepository;

  @Override
  public StoreEntity save(StoreEntity storeEntity) {
    return storeJpaRepository.save(storeEntity);
  }

  @Override
  public Optional<StoreEntity> findById(UUID storeId) {
    return storeJpaRepository.findById(storeId);
  }

  @Override
  public Page<StoreEntity> findAll(Pageable pageable) {
    return storeJpaRepository.findAll(pageable);
  }

  @Override
  public long count() {
    return storeJpaRepository.count();
  }
}