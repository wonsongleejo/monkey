package com.monkey.commonmodule.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
    // AuditorAware는 JWT 구현 이후에 설정!
}
