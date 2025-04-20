package com.monkey.storeservice.domain.repository;

import com.monkey.storeservice.domain.entity.StoreEntity;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StoreRepository {

  StoreEntity save(StoreEntity storeEntity);

  Optional<StoreEntity> findById(UUID storeId);

  Page<StoreEntity> findAll(Pageable pageable);

  long count();
}