package com.monkey.storeservice.domain.article.repository;

import com.monkey.storeservice.domain.article.entity.StoreEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StoreRepository {

  StoreEntity save(StoreEntity storeEntity);

  Optional<StoreEntity> findById(UUID storeId);

  List<StoreEntity> findAll();

  long count();
}