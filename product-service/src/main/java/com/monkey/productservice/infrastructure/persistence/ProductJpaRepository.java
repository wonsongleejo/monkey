package com.monkey.productservice.infrastructure.persistence;

import com.monkey.productservice.domain.entity.ProductEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, UUID> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select p from ProductEntity p where p.productId = :productId and p.isDeleted = false")
    Optional<ProductEntity> findByProductIdAndIsDeletedFalse(@Param("productId") UUID productId);

    List<ProductEntity> findAllByIsDeletedFalse();
    Page<ProductEntity> findAllByIsDeletedFalse(Pageable pageable);
}