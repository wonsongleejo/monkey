package com.monkey.storereservationservice.application.service;

import com.monkey.common_module.dto.ResponseCode;
import com.monkey.common_module.exception.CustomException;
import com.monkey.storereservationservice.application.dto.request.ReqStoreReservationPostDTOApiV1;
import com.monkey.storereservationservice.application.dto.response.ResStoreReservationGetByIdDTOApiV1;
import com.monkey.storereservationservice.application.dto.response.ResStoreReservationGetDTOApiV1;
import com.monkey.storereservationservice.application.dto.response.ResStoreReservationPostDTOApiV1;
import com.monkey.storereservationservice.application.dto.response.ResStoreReservationPutByIdStatusDTOApiV1;
import com.monkey.storereservationservice.domain.entity.StoreReservationEntity;
import com.monkey.storereservationservice.domain.repository.StoreReservationRepository;
import com.monkey.storereservationservice.domain.vo.StoreReservationStatus;
import com.monkey.storereservationservice.infrastructure.client.StoreClient;
import com.monkey.storereservationservice.infrastructure.dto.response.ResStoreTimeSlotDTOApiV1;
import com.monkey.storereservationservice.application.context.UserContext;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service("storeReservationServiceV3")
@Transactional
public class StoreReservationServiceImplApiV3 implements StoreReservationServiceApiV1 {

    private final StoreReservationRepository storeReservationRepository;
    private final StoreClient storeClient;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedissonClient redissonClient;

    private PersonInfo calculateCurrentAndMaxPerson(UUID timeSlotId) {
        String currentKey = "timeslot:" + timeSlotId + ":currentReservedPerson";
        String maxKey = "timeslot:" + timeSlotId + ":maxPerson";

        Integer currentReservedPerson = getValueFromRedis(currentKey);
        Integer maxPerson = getValueFromRedis(maxKey);

        if (currentReservedPerson == null) {
            currentReservedPerson = 0;
        }

        if (maxPerson == null) {
            // 캐시에 없으면 Feign Client 호출
            ResStoreTimeSlotDTOApiV1 timeSlotResponse = storeClient.getTimeSlotById(timeSlotId);
            ResStoreTimeSlotDTOApiV1.StoreTimeSlot timeSlot = timeSlotResponse.getData().getStoreTimeSlot();
            maxPerson = timeSlot.getMaxPerson();

            // Redis에 저장
            redisTemplate.opsForValue().set(maxKey, maxPerson);
        }

        return new PersonInfo(currentReservedPerson, maxPerson);
    }

    private Integer getValueFromRedis(String key) {
        Object value = redisTemplate.opsForValue().get(key);
        return value != null ? Integer.parseInt(value.toString()) : null;
    }

    private void updateCurrentReservedPerson(UUID timeSlotId, int newCount) {
        String currentKey = "timeslot:" + timeSlotId + ":currentReservedPerson";
        redisTemplate.opsForValue().set(currentKey, newCount);
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
        String lockKey = "lock:storeReservation:" + timeSlotId;
        RLock lock = redissonClient.getLock(lockKey);

        try {
            if (lock.tryLock(10, 5, TimeUnit.SECONDS)) { // 모든 요청에 무조건 락 걸기
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

                updateCurrentReservedPerson(timeSlotId, personInfo.getCurrentReservedPerson() + requestedPersonCount);

                return ResStoreReservationPostDTOApiV1.from(saved, personInfo.getCurrentReservedPerson() + requestedPersonCount, personInfo.getMaxPerson());
            } else {
                throw new CustomException(ResponseCode.STORE_RESERVATION_FULL); // 락 획득 실패
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Lock acquisition interrupted", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    // 실제 예약 처리 로직만 따로 뺀 메서드
    private ResStoreReservationPostDTOApiV1 reserve(UUID timeSlotId, Integer requestedPersonCount, UserContext userContext, PersonInfo personInfo) {
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

        // Redis에 현재 예약 인원 업데이트
        updateCurrentReservedPerson(timeSlotId, personInfo.getCurrentReservedPerson() + requestedPersonCount);

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

    // 예약 상태 변경 (예약 취소시 cancelReservation 호출)
    @Override
    public ResStoreReservationPutByIdStatusDTOApiV1 changeStatus(UserContext userContext, UUID storeReservationId, StoreReservationStatus status) {
        StoreReservationEntity entity = storeReservationRepository.findById(storeReservationId);
        if (entity == null) throw new CustomException(ResponseCode.NOT_FOUND);

        if (status == StoreReservationStatus.CANCELED) {
            return cancelReservation(entity);
        }

        entity.changeStatus(status);
        StoreReservationEntity saved = storeReservationRepository.save(entity);

        PersonInfo personInfo = calculateCurrentAndMaxPerson(saved.getTimeSlotId());
        return ResStoreReservationPutByIdStatusDTOApiV1.from(saved, personInfo.getCurrentReservedPerson(), personInfo.getMaxPerson());
    }

    // 예약 취소하는 비즈니스 로직 (캐시 수정 시 decreaseCurrentReservedPerson 호출)
    private ResStoreReservationPutByIdStatusDTOApiV1 cancelReservation(StoreReservationEntity entity) {
        if (entity.getStatus() == StoreReservationStatus.CANCELED) {
            throw new CustomException(ResponseCode.DUPLICATED_REQUEST);
        }

        int decreaseCount = entity.getPersonCount();
        decreaseCurrentReservedPerson(entity.getTimeSlotId(), decreaseCount);

        entity.changeStatus(StoreReservationStatus.CANCELED);
        StoreReservationEntity saved = storeReservationRepository.save(entity);

        PersonInfo personInfo = calculateCurrentAndMaxPerson(saved.getTimeSlotId());
        return ResStoreReservationPutByIdStatusDTOApiV1.from(saved, personInfo.getCurrentReservedPerson(), personInfo.getMaxPerson());
    }

    // 예약 취소로 인해 Redis 캐시만 수정하는 메서드
    private void decreaseCurrentReservedPerson(UUID timeSlotId, int decreaseCount) {
        String currentKey = "timeslot:" + timeSlotId + ":currentReservedPerson";
        Integer currentReservedPerson = getValueFromRedis(currentKey);

        if (currentReservedPerson == null) {
            currentReservedPerson = 0;
        }

        int newCount = Math.max(0, currentReservedPerson - decreaseCount);
        redisTemplate.opsForValue().set(currentKey, newCount);
    }

    @Override
    public ResStoreReservationGetDTOApiV1 getAllByUserIdAndStoreId(Long userId, UUID storeId) {
        List<ResStoreReservationGetDTOApiV1.StoreReservation> list = storeReservationRepository.findAll().stream()
                .filter(res -> res.getUserId().equals(userId))
                .map(entity -> {
                    try {
                        PersonInfo personInfo = calculateCurrentAndMaxPerson(entity.getTimeSlotId());

                        ResStoreTimeSlotDTOApiV1 timeSlotResponse = storeClient.getTimeSlotById(entity.getTimeSlotId());
                        ResStoreTimeSlotDTOApiV1.StoreTimeSlot timeSlot = timeSlotResponse.getData().getStoreTimeSlot();

                        return ResStoreReservationGetDTOApiV1.StoreReservation.builder()
                                .storeReservationId(entity.getStoreReservationId())
                                .status(entity.getStatus())
                                .currentReservedPerson(personInfo.getCurrentReservedPerson())
                                .maxPerson(personInfo.getMaxPerson())
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
                                                .userName("unknown")
                                                .build()
                                )
                                .build();
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(res -> res != null
                        && res.getTimeSlot() != null
                        && res.getTimeSlot().getStore() != null
                        && res.getTimeSlot().getStore().getStoreId().equals(storeId))
                .toList();

        return ResStoreReservationGetDTOApiV1.builder()
                .storeReservationList(list)
                .build();
    }
}