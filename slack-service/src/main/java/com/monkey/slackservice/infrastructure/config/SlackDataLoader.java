package com.monkey.slackservice.infrastructure.config;

import com.monkey.slackservice.domain.slack.entity.SlackEntity;
import com.monkey.slackservice.domain.slack.repository.SlackRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.UUID;

@Configuration
@Profile("dev")
public class SlackDataLoader {

    @Bean
    public CommandLineRunner listen(SlackRepository slackRepository) {
        return args -> {
            if (slackRepository.count() == 0) { // 데이터가 없을 경우에만 더미 데이터 추가
                SlackEntity slackEntity = SlackEntity.builder()
                        .userSlackId(UUID.randomUUID())
                        .slackMessage("예약이 완료되었습니다.")
                        .build();
                slackRepository.save(slackEntity);
            }
        };
    }
}