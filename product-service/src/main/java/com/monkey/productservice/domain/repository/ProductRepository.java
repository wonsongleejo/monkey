package com.monkey.productservice.domain.repository;

import com.monkey.productservice.domain.entity.ProductEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    ProductEntity save(ProductEntity productEntity);
    Optional<ProductEntity> findByProductIdAndIsDeletedFalse(UUID productId);
    List<ProductEntity> findAllByIsDeletedFalse();
}
