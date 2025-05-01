package com.monkey.slackservice.application.service;

import com.monkey.slackservice.application.dto.request.ReqSlackStoreMessageDTOApiV1;
import com.monkey.slackservice.application.dto.response.ResSlackMessageDTOApiV1;
import com.monkey.slackservice.domain.entity.SlackEntity;
import com.monkey.slackservice.domain.repository.SlackRepository;
import com.monkey.slackservice.infrastructure.client.StoreReservationClient;
import com.monkey.slackservice.infrastructure.dto.response.ResStoreReservationGetByIdDTOApiV1;
import com.monkey.slackservice.infrastructure.message.SlackMessageFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SlackServiceImpl implements SlackService {

    private final StoreReservationClient storeReservationClient;
    private final SlackMessageFormatter slackMessageFormatter;
    private final SlackRepository slackRepository;

    @Override
    public ResSlackMessageDTOApiV1 sendStoreReservationMessage(ReqSlackStoreMessageDTOApiV1 request) {
        var slackRequest = request.getSlack();

        ResStoreReservationGetByIdDTOApiV1.StoreReservation reservation = storeReservationClient
                .getById(slackRequest.getStoreReservationId())
                .getData()
                .getStoreReservation();

        String message = slackMessageFormatter.format(reservation.getStatus());

        SlackEntity savedEntity = slackRepository.save(SlackEntity.builder()
                .reservationId(slackRequest.getStoreReservationId())
                .slackId(slackRequest.getSlackId())
                .slackMessage(message)
                .build());

        return ResSlackMessageDTOApiV1.builder()
                .slackMessageId(savedEntity.getSlackMessageId())
                .slackMessage(savedEntity.getSlackMessage())
                .build();
    }
}