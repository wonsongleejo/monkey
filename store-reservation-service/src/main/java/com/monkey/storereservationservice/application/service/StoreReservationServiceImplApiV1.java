package com.monkey.storereservationservice.application.service;

import com.monkey.storereservationservice.application.dto.request.ReqStoreReservationPostDTOApiV1;
import com.monkey.storereservationservice.application.dto.response.ResStoreReservationGetByIdDTOApiV1;
import com.monkey.storereservationservice.application.dto.response.ResStoreReservationGetDTOApiV1;
import com.monkey.storereservationservice.application.dto.response.ResStoreReservationPostDTOApiV1;
import com.monkey.storereservationservice.domain.storereservation.entity.StoreReservationEntity;
import com.monkey.storereservationservice.domain.storereservation.repository.StoreReservationRepository;
import com.monkey.storereservationservice.domain.storereservation.vo.StoreReservationStatus;
import com.monkey.storereservationservice.infrastructure.client.SlackFeignClient;
import com.monkey.storereservationservice.infrastructure.client.StoreClient;
import com.monkey.storereservationservice.infrastructure.dto.request.ReqSlackStoreReservationPostDTOApiV1;
import com.monkey.storereservationservice.infrastructure.dto.response.ResStoreTimeSlotDTOApiV1;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Transactional
public class StoreReservationServiceImplApiV1 implements StoreReservationServiceApiV1 {

    private final StoreReservationRepository storeReservationRepository;
    private final SlackFeignClient slackFeignClient;
    private final StoreClient storeClient;

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

        slackFeignClient.notifySlack(
                ReqSlackStoreReservationPostDTOApiV1.builder()
                        .slack(
                                ReqSlackStoreReservationPostDTOApiV1.SlackMessage.builder()
                                        .slackId("U0123456789")
                                        .slackMessage("예약이 완료되었습니다. 예약번호: " + saved.getStoreReservationId())
                                        .build()
                        )
                        .build()
        );

        return ResStoreReservationPostDTOApiV1.from(saved);
    }

    @Override
    public ResStoreReservationGetDTOApiV1 getAll(Long userId, UUID storeId) {
        List<ResStoreReservationGetDTOApiV1.StoreReservation> list = storeReservationRepository.findAll().stream()
                .map(entity -> {
                    ResStoreTimeSlotDTOApiV1 timeSlot = storeClient.getTimeSlotById(entity.getTimeSlotId());
                    UUID resolvedStoreId = timeSlot.getStore() != null ? timeSlot.getStore().getStoreId() : null;

                    return ResStoreReservationGetDTOApiV1.StoreReservation.builder()
                            .storeReservationId(entity.getStoreReservationId())
                            .status(entity.getStatus())
                            .timeSlot(
                                    ResStoreReservationGetDTOApiV1.StoreReservation.TimeSlot.builder()
                                            .store(
                                                    ResStoreReservationGetDTOApiV1.StoreReservation.TimeSlot.Store.builder()
                                                            .storeId(resolvedStoreId)
                                                            .build()
                                            )
                                            .date(timeSlot.getDate())
                                            .entryTime(timeSlot.getEntryTime())
                                            .exitTime(timeSlot.getExitTime())
                                            .build()
                            )
                            .user(
                                    ResStoreReservationGetDTOApiV1.StoreReservation.User.builder()
                                            .userId(entity.getUserId())
                                            .userName("테스트유저")
                                            .build()
                            )
                            .build();
                })
                .toList();

        return ResStoreReservationGetDTOApiV1.builder()
                .storeReservationList(list)
                .build();
    }

    @Override
    public ResStoreReservationGetByIdDTOApiV1 getById(UUID storeReservationId) {
        StoreReservationEntity entity = storeReservationRepository.findById(storeReservationId);
        ResStoreTimeSlotDTOApiV1 timeSlot = storeClient.getTimeSlotById(entity.getTimeSlotId());
        UUID resolvedStoreId = timeSlot.getStore() != null ? timeSlot.getStore().getStoreId() : null;

        return ResStoreReservationGetByIdDTOApiV1.builder()
                .storeReservation(
                        ResStoreReservationGetByIdDTOApiV1.StoreReservation.builder()
                                .storeReservationId(entity.getStoreReservationId())
                                .status(entity.getStatus())
                                .timeSlot(
                                        ResStoreReservationGetByIdDTOApiV1.StoreReservation.TimeSlot.builder()
                                                .store(
                                                        ResStoreReservationGetByIdDTOApiV1.StoreReservation.TimeSlot.Store.builder()
                                                                .storeId(resolvedStoreId)
                                                                .build()
                                                )
                                                .date(timeSlot.getDate())
                                                .entryTime(timeSlot.getEntryTime())
                                                .exitTime(timeSlot.getExitTime())
                                                .build()
                                )
                                .user(
                                        ResStoreReservationGetByIdDTOApiV1.StoreReservation.User.builder()
                                                .userId(entity.getUserId())
                                                .userName("테스트유저")
                                                .build()
                                )
                                .build()
                )
                .build();
    }
}