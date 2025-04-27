package com.monkey.slackservice.application.service;

import com.monkey.slackservice.application.dto.request.ReqSlackStoreMessageDTOApiV1;
import com.monkey.slackservice.application.dto.response.ResSlackMessageDTOApiV1;

public interface SlackService {

    ResSlackMessageDTOApiV1 sendStoreReservationMessage(ReqSlackStoreMessageDTOApiV1 request);
}