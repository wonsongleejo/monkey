package com.monkey.slackservice.domain.entity;

import com.monkey.common_module.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "p_slack")
@SQLRestriction("deleted_at is null")
public class SlackEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID slackMessageId;

    @Column(nullable = false)
    private String slackId;

    @Column(nullable = false)
    private String slackMessage;

    @Column(nullable = false)
    private UUID reservationId;

    public static SlackEntity createSlackMessage(String slackId, String slackMessage, UUID reservationId) {
        return new SlackEntity(
                null,
                slackId,
                slackMessage,
                reservationId
        );
    }
}