package com.monkey.storeservice.application.service;

import com.monkey.common_module.dto.ResponseCode;
import com.monkey.common_module.exception.CustomException;
import com.monkey.storeservice.application.dto.request.ReqStoreTimeSlotPostDTOApiV1;
import com.monkey.storeservice.application.dto.request.ReqStoreTimeSlotPutDTOApiV1;
import com.monkey.storeservice.application.dto.response.ResStoreTimeSlotGetDTOApiV1;
import com.monkey.storeservice.application.dto.response.ResStoreTimeSlotPostDTOApiV1;
import com.monkey.storeservice.application.dto.response.ResStoreTimeSlotPutDTOApiV1;
import com.monkey.storeservice.domain.entity.StoreEntity;
import com.monkey.storeservice.domain.entity.StoreTimeSlotEntity;
import com.monkey.storeservice.domain.repository.StoreRepository;
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
  private final StoreRepository storeRepository;

  // 생성
  @Override
  public ResStoreTimeSlotPostDTOApiV1 postById(UUID storeId, ReqStoreTimeSlotPostDTOApiV1 reqDto,Long storeManagerId,String role) {

    // 헤더에서 role을 받아서 권한체크 ( 아무나 만들 수 없게 하기 위함)
    if(!"MANAGER".equals(role)) {
      throw new CustomException(ResponseCode.FORBIDDEN);
    }

    StoreTimeSlotEntity storeTimeSlotEntity = reqDto.getStoreTimeSlot().toStoreTimeSlotEntity();

    storeTimeSlotEntity.setStoreId(storeId);

    StoreTimeSlotEntity saved = storeTimeSlotRepository.save(storeTimeSlotEntity);

    return ResStoreTimeSlotPostDTOApiV1.of(saved);
  }

  // 수정
  @Override
  public ResStoreTimeSlotPutDTOApiV1 putById(UUID timeSlotId, ReqStoreTimeSlotPutDTOApiV1 reqDto,Long storeManagerId,UUID storeId,String role) {

    // 수정할 수 있는 storeId가 존재 하는지
    StoreEntity storeEntity = storeRepository.findById(storeId)
        .orElseThrow(()->new CustomException(ResponseCode.STORE_NOT_FOUND));

    // 수정할 수 있는 timeSlotId가 존재 하는지
    StoreTimeSlotEntity storeTimeSlotEntity = storeTimeSlotRepository.findById(timeSlotId)
        .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND));

    // 헤더에서 role을 받아서 권한체크 (아무나 수정 x)
    if (!"MANAGER".equals(role)) {
      throw new CustomException(ResponseCode.FORBIDDEN); // or ROLE_UNAUTHORIZED
    }

    // 헤더에서 storeManagerId를 받아서 자기의 스토어인지 확인
    if(!storeEntity.getStoreManagerId().equals(storeManagerId)) {
      throw new CustomException(ResponseCode.USER_NOT_FOUND);
    }

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
  public ResStoreTimeSlotGetDTOApiV1 getBy(UUID storeId, Pageable pageable) {

    // 조회할 수 있는 storeId가 존재 하는지
    StoreEntity storeEntity = storeRepository.findById(storeId)
        .orElseThrow(()->new CustomException(ResponseCode.STORE_NOT_FOUND));

    Page<StoreTimeSlotEntity> page = storeTimeSlotRepository.findAll(pageable);
    return ResStoreTimeSlotGetDTOApiV1.of(page);
  }
}
