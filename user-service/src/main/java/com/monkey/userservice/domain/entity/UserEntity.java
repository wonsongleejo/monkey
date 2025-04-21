package com.monkey.userservice.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "p_user")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    private String slackId;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("USER")
    private Role role;

    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean isDeleted;

    @Getter
    @AllArgsConstructor
    public enum Role{
        USER("USER"),
        MASTER("MASTER"),
        MANAGER("MANAGER")
        ;

        private final String value;
    }

    // 회원 정보 수정
    public void update(String password, String slackId) {
        this.password = password;
        this.slackId = slackId;
    }


    // 회원 삭제 (soft-delete)
    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }
}