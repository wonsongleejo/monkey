package com.monkey.productservice.infrastructure.persistence;

import com.monkey.productservice.domain.entity.ProductEntity;
import com.monkey.productservice.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {
    private final ProductJpaRepository productJpaRepository;

    @Override
    public ProductEntity save(ProductEntity productEntity) {
        return productJpaRepository.save(productEntity);
    }

    @Override
    public Optional<ProductEntity> findByProductIdAndIsDeletedFalse(UUID productId) {
        return productJpaRepository.findByProductIdAndIsDeletedFalse(productId);
    }

    @Override
    public List<ProductEntity> findAllByIsDeletedFalse() {
        return productJpaRepository.findAllByIsDeletedFalse();
    }
}
