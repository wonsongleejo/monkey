package com.monkey.storereservationservice.infrastructure.persistence.storereservation;

import com.monkey.storereservationservice.domain.storereservation.entity.StoreReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StoreReservationJpaRepository extends JpaRepository<StoreReservationEntity, UUID> {
}