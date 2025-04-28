package com.monkey.storereservationservice.infrastructure.persistence.storereservation;

import com.monkey.storereservationservice.domain.storereservation.entity.StoreReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface StoreReservationJpaRepository extends JpaRepository<StoreReservationEntity, UUID> {

    @Query("SELECT COALESCE(SUM(r.personCount), 0) FROM StoreReservationEntity r WHERE r.timeSlotId = :timeSlotId AND r.isDeleted = false")
    Integer sumPersonCountByTimeSlotId(@Param("timeSlotId") UUID timeSlotId);
}