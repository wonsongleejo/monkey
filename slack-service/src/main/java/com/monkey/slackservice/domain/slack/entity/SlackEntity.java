package com.monkey.slackservice.domain.slack.entity;

import com.monkey.commonmodule.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Table(name = "p_slack_message")
@SQLRestriction("deleted_at is null")
public class SlackEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID slackMessageId;

    @Column(nullable = false)
    private UUID userSlackId;

    @Column(nullable = false)
    private String slackMessage;

    public static SlackEntity createSlackMessage(UUID slackId, String slackMessage) {
        return SlackEntity.builder()
                .userSlackId(slackId)
                .slackMessage(slackMessage)
                .build();
    }
}
