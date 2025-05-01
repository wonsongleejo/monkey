package com.monkey.slackservice.domain.repository;

import com.monkey.slackservice.domain.entity.SlackEntity;

public interface SlackRepository {
    SlackEntity save(SlackEntity slackEntity);
    long count();
}