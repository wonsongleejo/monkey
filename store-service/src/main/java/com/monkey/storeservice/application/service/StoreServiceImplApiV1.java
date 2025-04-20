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

@Service
@RequiredArgsConstructor
public class StoreServiceImplApiV1 implements StoreServiceApiV1{

  private final StoreRepository storeRepository;

  // 팝업스토어 등록
  @Override
  public ResStorePostDTOApiV1 postBy(ReqStorePostDTOApiV1 reqDto) {

    StoreEntity storeEntity = reqDto.getStore().toEntity();

    StoreEntity saved = storeRepository.save(storeEntity);

    return ResStorePostDTOApiV1.of(saved);
  }

  //팝업스토어 수정
  @Override
  public ResStorePutDTOApiV1 putById(UUID storeId, ReqStorePutDTOApiV1 reqDto) {

    StoreEntity storeEntity = storeRepository.findById(storeId)
            .orElseThrow(()->new CustomException(ResponseCode.NOT_FOUND));

    reqDto.getStore().update(storeEntity);

    StoreEntity updated = storeRepository.save(storeEntity);

    return ResStorePutDTOApiV1.of(updated);
  }

  @Override
  public ResStoreGetDTOApiV1 getById(UUID storeId) {

    StoreEntity storeEntity = storeRepository.findById(storeId)
        .orElseThrow(()->new CustomException(ResponseCode.NOT_FOUND));

    return ResStoreGetDTOApiV1.of(storeEntity);
  }

  @Override
  public ResStoreGetDTOApiV1 getBy(Pageable pageable) {

    Page<StoreEntity> storePage = storeRepository.findAll(pageable);

    return ResStoreGetDTOApiV1.of(storePage);
  }
}
