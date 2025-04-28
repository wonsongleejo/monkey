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
import com.monkey.storereservationservice.infrastructure.client.StoreClient;
import com.monkey.storereservationservice.infrastructure.dto.response.ResStoreTimeSlotDTOApiV1;
import com.monkey.storereservationservice.infrastructure.security.UserContext;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service("storeReservationServiceV1")
@Transactional
public class StoreReservationServiceImplApiV1 implements StoreReservationServiceApiV1 {

    private final StoreReservationRepository storeReservationRepository;
    private final StoreClient storeClient;

    private PersonInfo calculateCurrentAndMaxPerson(UUID timeSlotId) {

        Integer currentReservedPersonCount = storeReservationRepository.sumPersonCountByTimeSlotId(timeSlotId);
        if (currentReservedPersonCount == null) {
            currentReservedPersonCount = 0;
        }

        ResStoreTimeSlotDTOApiV1 timeSlotResponse = storeClient.getTimeSlotById(timeSlotId);
        ResStoreTimeSlotDTOApiV1.StoreTimeSlot timeSlot = timeSlotResponse.getData().getStoreTimeSlot();
        Integer maxPerson = timeSlot.getMaxPerson();

        return new PersonInfo(currentReservedPersonCount, maxPerson);
    }

    @Getter
    @AllArgsConstructor
    private static class PersonInfo {
        private Integer currentReservedPerson;
        private Integer maxPerson;
    }

    @Override
    public ResStoreReservationPostDTOApiV1 create(ReqStoreReservationPostDTOApiV1 request, UserContext userContext) {
        UUID timeSlotId = request.getStoreReservation().getTimeSlotId();
        PersonInfo personInfo = calculateCurrentAndMaxPerson(timeSlotId);

        Integer requestedPersonCount = request.getStoreReservation().getPersonCount();

        if (personInfo.getCurrentReservedPerson() + requestedPersonCount > personInfo.getMaxPerson()) {
            throw new CustomException(ResponseCode.STORE_RESERVATION_FULL);
        }

        StoreReservationEntity entity = StoreReservationEntity.createStoreReservation(
                timeSlotId,
                userContext.getUserId(),
                requestedPersonCount,
                StoreReservationStatus.SCHEDULED
        );

        StoreReservationEntity saved = storeReservationRepository.save(entity);

        return ResStoreReservationPostDTOApiV1.from(saved, personInfo.getCurrentReservedPerson() + requestedPersonCount, personInfo.getMaxPerson());
    }

    @Override
    public ResStoreReservationGetDTOApiV1 getAll(UserContext userContext, UUID storeId) {
        List<ResStoreReservationGetDTOApiV1.StoreReservation> list = storeReservationRepository.findAll().stream()
                .filter(res -> res.getUserId().equals(userContext.getUserId()))
                .map(entity -> {
                    PersonInfo personInfo = calculateCurrentAndMaxPerson(entity.getTimeSlotId());

                    ResStoreTimeSlotDTOApiV1 timeSlotResponse = storeClient.getTimeSlotById(entity.getTimeSlotId());
                    ResStoreTimeSlotDTOApiV1.StoreTimeSlot timeSlot = timeSlotResponse.getData().getStoreTimeSlot();

                    return ResStoreReservationGetDTOApiV1.StoreReservation.builder()
                            .storeReservationId(entity.getStoreReservationId())
                            .status(entity.getStatus())
                            .currentReservedPerson(personInfo.getCurrentReservedPerson())
                            .maxPerson(personInfo.getMaxPerson())
                            .timeSlot(ResStoreReservationGetDTOApiV1.StoreReservation.TimeSlot.builder()
                                    .store(ResStoreReservationGetDTOApiV1.StoreReservation.TimeSlot.Store.builder()
                                            .storeId(timeSlot.getStoreId())
                                            .build())
                                    .date(timeSlot.getSlotDate())
                                    .entryTime(timeSlot.getEntryTime())
                                    .exitTime(timeSlot.getExitTime())
                                    .build())
                            .user(ResStoreReservationGetDTOApiV1.StoreReservation.User.builder()
                                    .userId(userContext.getUserId())
                                    .userName(userContext.getUserName())
                                    .build())
                            .build();
                }).toList();

        return ResStoreReservationGetDTOApiV1.builder()
                .storeReservationList(list)
                .build();
    }

    @Override
    public ResStoreReservationGetByIdDTOApiV1 getById(UserContext userContext, UUID storeReservationId) {
        StoreReservationEntity entity = storeReservationRepository.findById(storeReservationId);
        PersonInfo personInfo = calculateCurrentAndMaxPerson(entity.getTimeSlotId());

        ResStoreTimeSlotDTOApiV1 timeSlotResponse = storeClient.getTimeSlotById(entity.getTimeSlotId());
        ResStoreTimeSlotDTOApiV1.StoreTimeSlot timeSlot = timeSlotResponse.getData().getStoreTimeSlot();

        return ResStoreReservationGetByIdDTOApiV1.builder()
                .storeReservation(ResStoreReservationGetByIdDTOApiV1.StoreReservation.builder()
                        .storeReservationId(entity.getStoreReservationId())
                        .status(entity.getStatus())
                        .currentReservedPerson(personInfo.getCurrentReservedPerson())
                        .maxPerson(personInfo.getMaxPerson())
                        .timeSlot(ResStoreReservationGetByIdDTOApiV1.StoreReservation.TimeSlot.builder()
                                .store(ResStoreReservationGetByIdDTOApiV1.StoreReservation.TimeSlot.Store.builder()
                                        .storeId(timeSlot.getStoreId())
                                        .build())
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
        if (entity == null) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        entity.changeStatus(status);
        StoreReservationEntity saved = storeReservationRepository.save(entity);

        ResStoreTimeSlotDTOApiV1 timeSlotResponse = storeClient.getTimeSlotById(saved.getTimeSlotId());
        ResStoreTimeSlotDTOApiV1.StoreTimeSlot timeSlot = timeSlotResponse.getData().getStoreTimeSlot();

        Integer maxPerson = timeSlot.getMaxPerson();

        // DB 조회로 현재 예약된 인원 수 계산
        int currentReservedPerson = storeReservationRepository
                .findAll()
                .stream()
                .filter(res -> res.getTimeSlotId().equals(saved.getTimeSlotId()))
                .filter(res -> res.getStatus() == StoreReservationStatus.SCHEDULED)
                .mapToInt(StoreReservationEntity::getPersonCount)
                .sum();

        return ResStoreReservationPutByIdStatusDTOApiV1.from(saved, currentReservedPerson, maxPerson);
    }

    // feignClient: 상품 예약 시 스토어 예약내역 전체 조회 -> 스토어 예약한 회원인지 확인
    @Override
    public ResStoreReservationGetDTOApiV1 getAllByUserIdAndStoreId(Long userId, UUID storeId) {
        List<ResStoreReservationGetDTOApiV1.StoreReservation> list = storeReservationRepository.findAll().stream()
                .filter(res -> res.getUserId().equals(userId)) // userId 기준 필터링
                .map(entity -> {
                    try {
                        ResStoreTimeSlotDTOApiV1 timeSlotResponse = storeClient.getTimeSlotById(entity.getTimeSlotId());
                        if (timeSlotResponse == null || timeSlotResponse.getData() == null) {
                            throw new RuntimeException("TimeSlot not found: " + entity.getTimeSlotId());
                        }

                        ResStoreTimeSlotDTOApiV1.StoreTimeSlot timeSlot = timeSlotResponse.getData().getStoreTimeSlot();

                        return ResStoreReservationGetDTOApiV1.StoreReservation.builder()
                                .storeReservationId(entity.getStoreReservationId())
                                .status(entity.getStatus())
                                .timeSlot(
                                        ResStoreReservationGetDTOApiV1.StoreReservation.TimeSlot.builder()
                                                .store(
                                                        ResStoreReservationGetDTOApiV1.StoreReservation.TimeSlot.Store.builder()
                                                                .storeId(timeSlot.getStoreId())
                                                                .build()
                                                )
                                                .date(timeSlot.getSlotDate())
                                                .entryTime(timeSlot.getEntryTime())
                                                .exitTime(timeSlot.getExitTime())
                                                .build()
                                )
                                .user(
                                        ResStoreReservationGetDTOApiV1.StoreReservation.User.builder()
                                                .userId(userId)
                                                .userName("unknown") // 필요시 FeignClient로 조회 가능
                                                .build()
                                )
                                .build();
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(res ->
                        res != null &&
                                res.getTimeSlot() != null &&
                                res.getTimeSlot().getStore() != null &&
                                res.getTimeSlot().getStore().getStoreId().equals(storeId)
                )
                .toList();

        return ResStoreReservationGetDTOApiV1.builder()
                .storeReservationList(list)
                .build();
    }
}