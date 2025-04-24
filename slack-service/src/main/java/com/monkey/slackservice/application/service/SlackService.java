package com.monkey.slackservice.application.service;

import com.monkey.slackservice.application.dto.request.ReqSlackStoreReservationPostDTOApiV1;
import com.monkey.slackservice.application.dto.response.ResSlackProductReservationPostDTOApiV1;
import com.monkey.slackservice.application.dto.response.ResSlackStoreReservationPostDTOApiV1;

public interface SlackService {

    ResSlackStoreReservationPostDTOApiV1 notifyStoreReservationSlackAndSave(
            ReqSlackStoreReservationPostDTOApiV1 request,
            ResSlackStoreReservationPostDTOApiV1.StoreReservation reservationInfo
    );

    ResSlackProductReservationPostDTOApiV1 notifyProductReservationSlackAndSave(
            ReqSlackStoreReservationPostDTOApiV1 request,
            ResSlackProductReservationPostDTOApiV1.ProductReservation reservationInfo
    );
}