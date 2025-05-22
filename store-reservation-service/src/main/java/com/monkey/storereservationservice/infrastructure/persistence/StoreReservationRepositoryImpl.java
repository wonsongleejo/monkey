package com.monkey.storereservationservice.infrastructure.persistence;

import com.monkey.storereservationservice.domain.entity.StoreReservationEntity;
import com.monkey.storereservationservice.domain.repository.StoreReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
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
    public Integer sumPersonCountByTimeSlotId(UUID timeSlotId) {
        return storeReservationJpaRepository.sumPersonCountByTimeSlotId(timeSlotId);
    }

    @Override
    public List<StoreReservationEntity> findAll() {
        return storeReservationJpaRepository.findAll();
    }

    @Override
    public StoreReservationEntity findById(UUID storeReservationId) {
        return storeReservationJpaRepository.findById(storeReservationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 예약 ID입니다."));
    }

    @Override
    public long count() {
        return storeReservationJpaRepository.count();
    }
}