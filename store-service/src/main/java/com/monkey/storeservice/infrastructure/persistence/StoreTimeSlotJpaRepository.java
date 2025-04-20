package com.monkey.storeservice.infrastructure.persistence;

import com.monkey.storeservice.domain.entity.StoreTimeSlotEntity;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreTimeSlotJpaRepository extends JpaRepository<StoreTimeSlotEntity, UUID> {

}