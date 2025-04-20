package com.monkey.slackservice.application.service;

import com.monkey.slackservice.application.dto.request.ReqSlackStoreReservationPostDTOApiV1;
import com.monkey.slackservice.application.dto.response.ResSlackStoreReservationPostDTOApiV1;
import com.monkey.slackservice.domain.slack.entity.SlackEntity;
import com.monkey.slackservice.domain.slack.repository.SlackRepository;
import com.monkey.slackservice.infrastructure.slack.SlackClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SlackServiceImpl implements SlackService {

    private final SlackClient slackClient;
    private final SlackRepository slackRepository;

    @Override
    public ResSlackStoreReservationPostDTOApiV1 notifySlackAndSave(
            ReqSlackStoreReservationPostDTOApiV1 request,
            ResSlackStoreReservationPostDTOApiV1.StoreReservation reservationInfo
    ) {
        String slackId = request.getSlack().getSlackId();
        String message = request.getSlack().getSlackMessage();

        slackClient.sendSlackMessage(slackId, message);

        SlackEntity entity = SlackEntity.createSlackMessage(slackId, message);
        slackRepository.save(entity);

        return ResSlackStoreReservationPostDTOApiV1.of(reservationInfo);
    }
}