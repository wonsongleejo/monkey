package com.monkey.slackservice.infrastructure.persistence.slack;

import com.monkey.slackservice.domain.slack.entity.SlackEntity;
import com.monkey.slackservice.domain.slack.repository.SlackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SlackRepositoryImpl implements SlackRepository {

    private final SlackJpaRepository slackJpaRepository;

    @Override
    public SlackEntity save(SlackEntity slackEntity) {
        return slackJpaRepository.save(slackEntity);
    }
}