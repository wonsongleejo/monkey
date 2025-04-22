package com.monkey.storereservationservice.application.service;

import com.monkey.storereservationservice.application.dto.request.ReqStoreReservationPostDTOApiV1;
import com.monkey.storereservationservice.application.dto.response.ResStoreReservationGetByIdDTOApiV1;
import com.monkey.storereservationservice.application.dto.response.ResStoreReservationGetDTOApiV1;
import com.monkey.storereservationservice.application.dto.response.ResStoreReservationPostDTOApiV1;
import com.monkey.storereservationservice.application.dto.response.ResStoreReservationPutByIdStatusDTOApiV1;
import com.monkey.storereservationservice.domain.storereservation.vo.StoreReservationStatus;
import com.monkey.storereservationservice.infrastructure.security.UserContext;

import java.util.UUID;

public interface StoreReservationServiceApiV1 {
    ResStoreReservationPostDTOApiV1 create(ReqStoreReservationPostDTOApiV1 request, UserContext userContext);
    ResStoreReservationGetDTOApiV1 getAll(UserContext userContext, Long userId, UUID storeId);
    ResStoreReservationGetByIdDTOApiV1 getById(UserContext userContext, UUID storeReservationId);
    ResStoreReservationPutByIdStatusDTOApiV1 changeStatus(UserContext userContext, UUID storeReservationId, StoreReservationStatus status);
}