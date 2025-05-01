package com.monkey.slackservice.infrastructure.persistence.slack;

import com.monkey.slackservice.domain.entity.SlackEntity;
import com.monkey.slackservice.domain.repository.SlackRepository;
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

    @Override
    public long count() {
        return slackJpaRepository.count();
    }
}