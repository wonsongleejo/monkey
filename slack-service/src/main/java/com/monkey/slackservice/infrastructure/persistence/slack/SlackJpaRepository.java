package com.monkey.slackservice.infrastructure.persistence.slack;

import com.monkey.slackservice.domain.slack.entity.SlackEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SlackJpaRepository extends JpaRepository<SlackEntity, UUID> {
}