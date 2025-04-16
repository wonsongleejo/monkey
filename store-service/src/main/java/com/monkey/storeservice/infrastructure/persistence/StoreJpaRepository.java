package com.monkey.storeservice.infrastructure.persistence;

import com.monkey.storeservice.domain.article.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StoreJpaRepository extends JpaRepository<StoreEntity, UUID> {

}