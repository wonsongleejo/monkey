package com.monkey.storeservice.application.service;

import com.monkey.common_module.dto.ResponseCode;
import com.monkey.common_module.exception.CustomException;
import com.monkey.storeservice.application.dto.request.ReqStorePostDTOApiV1;
import com.monkey.storeservice.application.dto.request.ReqStorePutDTOApiV1;
import com.monkey.storeservice.application.dto.response.ResStoreGetDTOApiV1;
import com.monkey.storeservice.application.dto.response.ResStorePostDTOApiV1;
import com.monkey.storeservice.application.dto.response.ResStorePutDTOApiV1;
import com.monkey.storeservice.domain.entity.StoreEntity;
import com.monkey.storeservice.domain.repository.StoreRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

@Service
@RequiredArgsConstructor
public class StoreServiceImplApiV1 implements StoreServiceApiV1{

  private final StoreRepository storeRepository;

  // 팝업스토어 등록
  @Override
  public ResStorePostDTOApiV1 postBy(ReqStorePostDTOApiV1 reqDto,Long storeManagerId,String role) {

    // 헤더에서 role을 받아서 권한체크 ( 아무나 만들 수 없게 하기 위함)
    if(!"MANAGER".equals(role)) {
      throw new CustomException(ResponseCode.FORBIDDEN);
    }

    StoreEntity storeEntity = reqDto.getStore().toEntity();

    // 헤더에서 storeManagerId를 받아서 등록
    storeEntity.setStoreManagerId(storeManagerId);

    StoreEntity saved = storeRepository.save(storeEntity);

    return ResStorePostDTOApiV1.of(saved);
  }

  //팝업스토어 수정
  @Override
  public ResStorePutDTOApiV1 putById(UUID storeId, ReqStorePutDTOApiV1 reqDto,Long storeManagerId,String role) {

    // 수정할 수 있는 storeId가 존재 하는지
    StoreEntity storeEntity = storeRepository.findById(storeId)
            .orElseThrow(()->new CustomException(ResponseCode.STORE_NOT_FOUND));

    // 헤더에서 role을 받아서 권한체크 (아무나 수정 x)
    if (!"MANAGER".equals(role)) {
      throw new CustomException(ResponseCode.FORBIDDEN); // or ROLE_UNAUTHORIZED
    }

    // 헤더에서 storeManagerId를 받아서 자기의 스토어인지 확인
    if(!storeEntity.getStoreManagerId().equals(storeManagerId)) {
      throw new CustomException(ResponseCode.USER_NOT_FOUND);
    }

    reqDto.getStore().update(storeEntity);

    StoreEntity updated = storeRepository.save(storeEntity);

    return ResStorePutDTOApiV1.of(updated);
  }

  @Override
  public ResStoreGetDTOApiV1 getById(UUID storeId) {

    StoreEntity storeEntity = storeRepository.findById(storeId)
        .orElseThrow(()->new CustomException(ResponseCode.STORE_NOT_FOUND));

    return ResStoreGetDTOApiV1.of(storeEntity);
  }

  @Override
  public ResStoreGetDTOApiV1 getBy(Pageable pageable) {

    Page<StoreEntity> storePage = storeRepository.findAll(pageable);

    return ResStoreGetDTOApiV1.of(storePage);
  }
}
