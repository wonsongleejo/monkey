package com.monkey.slackservice.domain.slack.repository;

import com.monkey.slackservice.domain.slack.entity.SlackEntity;

public interface SlackRepository {
    SlackEntity save(SlackEntity slackEntity);
    long count();
}