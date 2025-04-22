package com.monkey.storeservice.application.service;

import com.monkey.storeservice.application.dto.request.ReqStorePostDTOApiV1;
import com.monkey.storeservice.application.dto.request.ReqStorePutDTOApiV1;
import com.monkey.storeservice.application.dto.response.ResStoreGetDTOApiV1;
import com.monkey.storeservice.application.dto.response.ResStorePostDTOApiV1;
import com.monkey.storeservice.application.dto.response.ResStorePutDTOApiV1;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface StoreServiceApiV1 {

  ResStorePostDTOApiV1 postBy(ReqStorePostDTOApiV1 reqDto,Long storeManagerId,String role);

  ResStorePutDTOApiV1 putById(UUID storeId, ReqStorePutDTOApiV1 reqDto,Long storeManagerId,String role);

  ResStoreGetDTOApiV1 getById(UUID storeId);

  ResStoreGetDTOApiV1 getBy(Pageable pageable);

}
