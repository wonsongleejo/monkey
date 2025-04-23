package com.monkey.slackservice.application.service;

import com.monkey.slackservice.application.dto.request.ReqSlackStoreReservationPostDTOApiV1;
import com.monkey.slackservice.application.dto.request.ReqSlackStoreReservationPostDTOApiV1.SlackMessage;
import com.monkey.slackservice.application.dto.response.ResSlackStoreReservationPostDTOApiV1;
import com.monkey.slackservice.application.dto.response.ResSlackProductReservationPostDTOApiV1;
import com.monkey.slackservice.domain.slack.entity.SlackEntity;
import com.monkey.slackservice.domain.slack.repository.SlackRepository;
import com.monkey.slackservice.infrastructure.slack.SlackClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class SlackServiceImpl implements SlackService {

    private final SlackRepository slackRepository;
    private final SlackClient slackClient;

    @Override
    public ResSlackStoreReservationPostDTOApiV1 notifyStoreReservationSlackAndSave(
            ReqSlackStoreReservationPostDTOApiV1 request,
            ResSlackStoreReservationPostDTOApiV1.StoreReservation reservationInfo
    ) {
        SlackMessage slack = request.getSlack();

        slackClient.sendSlackMessage(slack.getSlackId(), slack.getSlackMessage());

        SlackEntity entity = SlackEntity.createSlackMessage(
                slack.getSlackId(),
                slack.getSlackMessage(),
                slack.getSlackMessageType()
        );

        slackRepository.save(entity);

        return ResSlackStoreReservationPostDTOApiV1.of(
                slack.getSlackMessage(),
                slack.getSlackMessageType().name(),
                UUID.randomUUID(),
                reservationInfo
        );
    }

    @Override
    public ResSlackProductReservationPostDTOApiV1 notifyProductReservationSlackAndSave(
            ReqSlackStoreReservationPostDTOApiV1 request,
            ResSlackProductReservationPostDTOApiV1.ProductReservation reservationInfo
    ) {
        SlackMessage slack = request.getSlack();

        slackClient.sendSlackMessage(slack.getSlackId(), slack.getSlackMessage());

        SlackEntity entity = SlackEntity.createSlackMessage(
                slack.getSlackId(),
                slack.getSlackMessage(),
                slack.getSlackMessageType()
        );

        slackRepository.save(entity);

        return ResSlackProductReservationPostDTOApiV1.of(
                slack.getSlackMessage(),
                slack.getSlackMessageType().name(),
                UUID.randomUUID(),
                reservationInfo
        );
    }
}