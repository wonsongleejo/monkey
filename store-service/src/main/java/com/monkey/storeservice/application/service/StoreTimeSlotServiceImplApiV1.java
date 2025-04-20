package com.monkey.storeservice.application.service;

import com.monkey.common_module.dto.ResponseCode;
import com.monkey.common_module.exception.CustomException;
import com.monkey.storeservice.application.dto.request.ReqStoreTimeSlotPostDTOApiV1;
import com.monkey.storeservice.application.dto.request.ReqStoreTimeSlotPutDTOApiV1;
import com.monkey.storeservice.application.dto.response.ResStoreTimeSlotGetDTOApiV1;
import com.monkey.storeservice.application.dto.response.ResStoreTimeSlotPostDTOApiV1;
import com.monkey.storeservice.application.dto.response.ResStoreTimeSlotPutDTOApiV1;
import com.monkey.storeservice.domain.entity.StoreTimeSlotEntity;
import com.monkey.storeservice.domain.repository.StoreTimeSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreTimeSlotServiceImplApiV1 implements StoreTimeSlotServiceApiV1 {

  private final StoreTimeSlotRepository storeTimeSlotRepository;

  // 생성
  @Override
  public ResStoreTimeSlotPostDTOApiV1 postById(UUID storeId, ReqStoreTimeSlotPostDTOApiV1 reqDto) {

    StoreTimeSlotEntity storeTimeSlotEntity = reqDto.getStoreTimeSlot().toStoreTimeSlotEntity();

    storeTimeSlotEntity.setStoreId(storeId);

    StoreTimeSlotEntity saved = storeTimeSlotRepository.save(storeTimeSlotEntity);

    return ResStoreTimeSlotPostDTOApiV1.of(saved);
  }

  // 수정
  @Override
  public ResStoreTimeSlotPutDTOApiV1 putById(UUID timeSlotId, ReqStoreTimeSlotPutDTOApiV1 reqDto) {

    StoreTimeSlotEntity storeTimeSlotEntity = storeTimeSlotRepository.findById(timeSlotId)
        .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND));

    reqDto.getStoreTimeSlot().update(storeTimeSlotEntity);

    StoreTimeSlotEntity updated = storeTimeSlotRepository.save(storeTimeSlotEntity);

    return ResStoreTimeSlotPutDTOApiV1.of(updated);
  }

  // 단건 조회
  @Override
  public ResStoreTimeSlotGetDTOApiV1 getById(UUID timeSlotId) {

    StoreTimeSlotEntity storeTimeSlotEntity = storeTimeSlotRepository.findById(timeSlotId)
        .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND));

    return ResStoreTimeSlotGetDTOApiV1.of(storeTimeSlotEntity);
  }

  // 전체 조회
  @Override
  public ResStoreTimeSlotGetDTOApiV1 getBy(Pageable pageable) {
    Page<StoreTimeSlotEntity> page = storeTimeSlotRepository.findAll(pageable);
    return ResStoreTimeSlotGetDTOApiV1.of(page);
  }
}
