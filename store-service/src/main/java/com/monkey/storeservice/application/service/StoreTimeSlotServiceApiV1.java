package com.monkey.storeservice.application.service;

import com.monkey.storeservice.application.dto.request.ReqStoreTimeSlotPostDTOApiV1;
import com.monkey.storeservice.application.dto.request.ReqStoreTimeSlotPutDTOApiV1;
import com.monkey.storeservice.application.dto.response.ResStoreTimeSlotGetDTOApiV1;
import com.monkey.storeservice.application.dto.response.ResStoreTimeSlotPostDTOApiV1;
import com.monkey.storeservice.application.dto.response.ResStoreTimeSlotPutDTOApiV1;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface StoreTimeSlotServiceApiV1 {

  ResStoreTimeSlotPostDTOApiV1 postById(UUID storeId, ReqStoreTimeSlotPostDTOApiV1 reqDto,Long storeManagerId,String role);

  ResStoreTimeSlotPutDTOApiV1 putById(UUID timeSlotId, ReqStoreTimeSlotPutDTOApiV1 reqDto,Long storeManagerId,UUID storeId,String role);

  ResStoreTimeSlotGetDTOApiV1 getById(UUID timeSlotId);

  ResStoreTimeSlotGetDTOApiV1 getBy(UUID storeId,Pageable pageable);
}
