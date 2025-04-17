package com.monkey.storereservationservice.application.service;

import com.monkey.storereservationservice.application.dto.request.ReqStoreReservationPostDTOApiV1;
import com.monkey.storereservationservice.application.dto.response.ResStoreReservationGetByIdDTOApiV1;
import com.monkey.storereservationservice.application.dto.response.ResStoreReservationGetDTOApiV1;
import com.monkey.storereservationservice.application.dto.response.ResStoreReservationPostByIdCancelDTOApiV1;
import com.monkey.storereservationservice.application.dto.response.ResStoreReservationPostDTOApiV1;
import com.monkey.storereservationservice.domain.storereservation.entity.StoreReservationEntity;
import com.monkey.storereservationservice.domain.storereservation.repository.StoreReservationRepository;
import com.monkey.storereservationservice.domain.storereservation.vo.StoreReservationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Transactional
public class StoreReservationServiceImplApiV1 implements StoreReservationServiceApiV1 {

    private final StoreReservationRepository storeReservationRepository;

    @Override
    public ResStoreReservationPostDTOApiV1 create(ReqStoreReservationPostDTOApiV1 request) {
        ReqStoreReservationPostDTOApiV1.StoreReservation storeReservation = request.getStoreReservation();

        StoreReservationEntity storeReservationEntity = StoreReservationEntity.createStoreReservation(
                storeReservation.getTimeSlotId(),
                1L,
                storeReservation.getPersonCount(),
                StoreReservationStatus.SCHEDULED
        );

        StoreReservationEntity saved = storeReservationRepository.save(storeReservationEntity);
        return ResStoreReservationPostDTOApiV1.from(saved);
    }

    @Override
    public ResStoreReservationPostByIdCancelDTOApiV1 cancel(UUID storeReservationId) {
        StoreReservationEntity storeReservationEntity = storeReservationRepository.findById(storeReservationId);
        storeReservationEntity.changeStatus(StoreReservationStatus.CANCELED);
        storeReservationRepository.save(storeReservationEntity);
        return ResStoreReservationPostByIdCancelDTOApiV1.from(storeReservationEntity);
    }

    @Override
    public ResStoreReservationGetDTOApiV1 getAll(Long userId, UUID storeId) {
        List<ResStoreReservationGetDTOApiV1.StoreReservation> list = storeReservationRepository.findAll().stream()
                .map(storeReservationEntity -> ResStoreReservationGetDTOApiV1.StoreReservation.builder()
                        .storeReservationId(storeReservationEntity.getStoreReservationId())
                        .status(storeReservationEntity.getStatus())
                        .timeSlot(
                                ResStoreReservationGetDTOApiV1.StoreReservation.TimeSlot.builder()
                                        .store(
                                                ResStoreReservationGetDTOApiV1.StoreReservation.TimeSlot.Store.builder()
                                                        .storeId(UUID.randomUUID())
                                                        .build()
                                        )
                                        .date(LocalDate.parse("2025-04-19"))
                                        .entryTime(LocalTime.parse("10:00:00"))
                                        .exitTime(LocalTime.parse("11:00:00"))
                                        .build()
                        )
                        .user(
                                ResStoreReservationGetDTOApiV1.StoreReservation.User.builder()
                                        .userId(storeReservationEntity.getUserId())
                                        .userName("테스트유저")
                                        .build()
                        )
                        .build()
                )
                .toList();

        return ResStoreReservationGetDTOApiV1.builder()
                .storeReservationList(list)
                .build();

    }

    @Override
    public ResStoreReservationGetByIdDTOApiV1 getById(UUID storeReservationId) {
        StoreReservationEntity storeReservationEntity = storeReservationRepository.findById(storeReservationId);
        return ResStoreReservationGetByIdDTOApiV1.builder()
                .storeReservation(
                        ResStoreReservationGetByIdDTOApiV1.StoreReservation.builder()
                                .storeReservationId(storeReservationEntity.getStoreReservationId())
                                .status(storeReservationEntity.getStatus())
                                .timeSlot(
                                        ResStoreReservationGetByIdDTOApiV1.StoreReservation.TimeSlot.builder()
                                                .store(
                                                        ResStoreReservationGetByIdDTOApiV1.StoreReservation.TimeSlot.Store.builder()
                                                                .storeId(UUID.randomUUID())
                                                                .build()
                                                )
                                                .date(LocalDate.parse("2025-04-19"))
                                                .entryTime(LocalTime.parse("10:00:00"))
                                                .exitTime(LocalTime.parse("11:00:00"))
                                                .build()
                                )
                                .user(
                                        ResStoreReservationGetByIdDTOApiV1.StoreReservation.User.builder()
                                                .userId(1L)
                                                .userName("테스트유저")
                                                .build()
                                )
                                .build()
                )
                .build();
    }
}