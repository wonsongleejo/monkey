package com.monkey.userservice.infrastructure.config;

import com.monkey.userservice.domain.entity.UserEntity;
import com.monkey.userservice.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@Profile("test")
public class UserDataLoader {

    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner loadData(UserRepository userRepository) {
        return args -> {
            if(userRepository.count() == 0){
                UserEntity master = UserEntity.builder()
                        .username("testmaster")
                        .password(passwordEncoder.encode("Test1234!@"))
                        .slackId("slackIdMaster")
                        .role(UserEntity.Role.MASTER)
                        .build();
                userRepository.save(master);

                UserEntity user1 = UserEntity.builder()
                        .username("testuser1")
                        .password(passwordEncoder.encode("Test1234!@"))
                        .slackId("slackId1")
                        .role(UserEntity.Role.USER)
                        .build();
                userRepository.save(user1);

                UserEntity user2 = UserEntity.builder()
                        .username("testuser2")
                        .password(passwordEncoder.encode("Test1234!@"))
                        .slackId("slackId1")
                        .role(UserEntity.Role.USER)
                        .build();
                userRepository.save(user2);

                UserEntity manager = UserEntity.builder()
                        .username("testmanage")
                        .password(passwordEncoder.encode("Test1234!@"))
                        .slackId("slackIdManager")
                        .role(UserEntity.Role.MANAGER)
                        .build();
                userRepository.save(manager);
            }
        };
    }
}
