package com.monkey.storereservationservice.application.service;

import com.monkey.common_module.dto.ResponseCode;
import com.monkey.common_module.exception.CustomException;
import com.monkey.storereservationservice.application.dto.request.ReqStoreReservationPostDTOApiV1;
import com.monkey.storereservationservice.application.dto.response.ResStoreReservationGetByIdDTOApiV1;
import com.monkey.storereservationservice.application.dto.response.ResStoreReservationGetDTOApiV1;
import com.monkey.storereservationservice.application.dto.response.ResStoreReservationPostDTOApiV1;
import com.monkey.storereservationservice.application.dto.response.ResStoreReservationPutByIdStatusDTOApiV1;
import com.monkey.storereservationservice.domain.storereservation.entity.StoreReservationEntity;
import com.monkey.storereservationservice.domain.storereservation.repository.StoreReservationRepository;
import com.monkey.storereservationservice.domain.storereservation.vo.StoreReservationStatus;
import com.monkey.storereservationservice.infrastructure.client.SlackFeignClient;
import com.monkey.storereservationservice.infrastructure.client.StoreClient;
import com.monkey.storereservationservice.infrastructure.dto.request.ReqSlackStoreReservationPostDTOApiV1;
import com.monkey.storereservationservice.infrastructure.dto.response.ResStoreTimeSlotDTOApiV1;
import com.monkey.storereservationservice.infrastructure.security.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class StoreReservationServiceImplApiV1 implements StoreReservationServiceApiV1 {

    private final StoreReservationRepository storeReservationRepository;
    private final SlackFeignClient slackFeignClient;
    private final StoreClient storeClient;

    @Override
    public ResStoreReservationPostDTOApiV1 create(ReqStoreReservationPostDTOApiV1 request, UserContext userContext) {
        ResStoreTimeSlotDTOApiV1 timeSlotResponse = storeClient.getTimeSlotById(request.getStoreReservation().getTimeSlotId());
        log.info(">> TimeSlot from StoreClient = {}", timeSlotResponse);

        ResStoreTimeSlotDTOApiV1.StoreTimeSlot timeSlot = timeSlotResponse.getData().getStoreTimeSlot();
        UUID timeSlotId = timeSlot.getTimeSlotId();

        StoreReservationEntity entity = StoreReservationEntity.createStoreReservation(
                timeSlotId,
                userContext.getUserId(),
                request.getStoreReservation().getPersonCount(),
                StoreReservationStatus.SCHEDULED
        );

        StoreReservationEntity saved = storeReservationRepository.save(entity);

        slackFeignClient.notifySlack(
                ReqSlackStoreReservationPostDTOApiV1.builder()
                        .slack(
                                ReqSlackStoreReservationPostDTOApiV1.SlackMessage.builder()
                                        .slackId("U0123456789")
                                        .slackMessage("예약이 완료되었습니다. 예약번호: " + saved.getStoreReservationId())
                                        .build()
                        ).build()
        );

        return ResStoreReservationPostDTOApiV1.from(saved);
    }

    @Override
    public ResStoreReservationGetDTOApiV1 getAll(UserContext userContext, Long userId, UUID storeId) {
        List<ResStoreReservationGetDTOApiV1.StoreReservation> list = storeReservationRepository.findAll().stream()
                .filter(res -> res.getUserId().equals(userId))
                .map(entity -> {
                    ResStoreTimeSlotDTOApiV1 timeSlotResponse = storeClient.getTimeSlotById(entity.getTimeSlotId());
                    ResStoreTimeSlotDTOApiV1.StoreTimeSlot timeSlot = timeSlotResponse.getData().getStoreTimeSlot();

                    return ResStoreReservationGetDTOApiV1.StoreReservation.builder()
                            .storeReservationId(entity.getStoreReservationId())
                            .status(entity.getStatus())
                            .timeSlot(ResStoreReservationGetDTOApiV1.StoreReservation.TimeSlot.builder()
                                    .store(ResStoreReservationGetDTOApiV1.StoreReservation.TimeSlot.Store.builder()
                                            .storeId(timeSlot.getStoreId()).build())
                                    .date(timeSlot.getSlotDate())
                                    .entryTime(timeSlot.getEntryTime())
                                    .exitTime(timeSlot.getExitTime())
                                    .build())
                            .user(ResStoreReservationGetDTOApiV1.StoreReservation.User.builder()
                                    .userId(userId)
                                    .userName(userContext.getUserName())
                                    .build())
                            .build();
                }).toList();

        return ResStoreReservationGetDTOApiV1.builder().storeReservationList(list).build();
    }

    @Override
    public ResStoreReservationGetByIdDTOApiV1 getById(UserContext userContext, UUID storeReservationId) {
        StoreReservationEntity entity = storeReservationRepository.findById(storeReservationId);
        ResStoreTimeSlotDTOApiV1 timeSlotResponse = storeClient.getTimeSlotById(entity.getTimeSlotId());
        ResStoreTimeSlotDTOApiV1.StoreTimeSlot timeSlot = timeSlotResponse.getData().getStoreTimeSlot();

        return ResStoreReservationGetByIdDTOApiV1.builder()
                .storeReservation(ResStoreReservationGetByIdDTOApiV1.StoreReservation.builder()
                        .storeReservationId(entity.getStoreReservationId())
                        .status(entity.getStatus())
                        .timeSlot(ResStoreReservationGetByIdDTOApiV1.StoreReservation.TimeSlot.builder()
                                .store(ResStoreReservationGetByIdDTOApiV1.StoreReservation.TimeSlot.Store.builder()
                                        .storeId(timeSlot.getStoreId()).build())
                                .date(timeSlot.getSlotDate())
                                .entryTime(timeSlot.getEntryTime())
                                .exitTime(timeSlot.getExitTime())
                                .build())
                        .user(ResStoreReservationGetByIdDTOApiV1.StoreReservation.User.builder()
                                .userId(userContext.getUserId())
                                .userName(userContext.getUserName())
                                .build())
                        .build())
                .build();
    }

    @Override
    public ResStoreReservationPutByIdStatusDTOApiV1 changeStatus(UserContext userContext, UUID storeReservationId, StoreReservationStatus status) {
        StoreReservationEntity entity = storeReservationRepository.findById(storeReservationId);
        if (entity == null) throw new CustomException(ResponseCode.NOT_FOUND);
        entity.changeStatus(status);
        return ResStoreReservationPutByIdStatusDTOApiV1.from(storeReservationRepository.save(entity));
    }
}