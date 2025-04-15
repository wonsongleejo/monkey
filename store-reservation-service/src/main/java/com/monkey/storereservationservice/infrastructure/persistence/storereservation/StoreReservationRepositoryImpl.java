package com.monkey.storereservationservice.infrastructure.persistence.storereservation;

import com.monkey.storereservationservice.domain.StoreReservation.entity.StoreReservationEntity;
import com.monkey.storereservationservice.domain.StoreReservation.repository.StoreReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class StoreReservationRepositoryImpl implements StoreReservationRepository {

    private final StoreReservationJpaRepository storeReservationJpaRepository;

    @Override
    public StoreReservationEntity save(StoreReservationEntity storeReservationEntity) {
        return storeReservationJpaRepository.save(storeReservationEntity);
    }

    @Override
    public StoreReservationEntity findById(UUID storeReservationId) {
        return storeReservationJpaRepository.findById(storeReservationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 예약 ID입니다."));
    }
}