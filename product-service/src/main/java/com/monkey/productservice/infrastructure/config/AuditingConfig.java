package com.monkey.productservice.infrastructure.config;

import com.monkey.common_module.entity.AuditorAwareImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditingConfig {

    @Bean(name = "auditorProvider")
    public AuditorAware<Long> auditorProvider() {
        return new AuditorAwareImpl();
    }
}
