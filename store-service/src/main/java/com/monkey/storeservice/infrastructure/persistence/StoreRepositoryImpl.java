package com.monkey.storeservice.infrastructure.persistence;

import com.monkey.storeservice.domain.article.entity.StoreEntity;
import com.monkey.storeservice.domain.article.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
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
  public List<StoreEntity> findAll() {
    return storeJpaRepository.findAll();
  }

  @Override
  public long count() {
    return storeJpaRepository.count();
  }
}