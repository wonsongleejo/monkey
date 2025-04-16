package com.monkey.userservice.infrastructure.config;

import com.monkey.userservice.domain.entity.UserEntity;
import com.monkey.userservice.domain.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class UserDataLoader {

    @Bean
    public CommandLineRunner loadData(UserRepository userRepository) {
        return args -> {
            if(userRepository.count() == 0){
                UserEntity master = UserEntity.builder()
                        .username("testmaster")
                        .password("test1234")
                        .slackId("slackIdMaster")
                        .role(UserEntity.Role.MASTER)
                        .build();
                userRepository.save(master);

                UserEntity user1 = UserEntity.builder()
                        .username("testuser1")
                        .password("test1234")
                        .slackId("slackId1")
                        .role(UserEntity.Role.USER)
                        .build();
                userRepository.save(user1);

                UserEntity user2 = UserEntity.builder()
                        .username("testuser2")
                        .password("test1234")
                        .slackId("slackId1")
                        .role(UserEntity.Role.USER)
                        .build();
                userRepository.save(user2);

                UserEntity manager = UserEntity.builder()
                        .username("testmanager")
                        .password("test1234")
                        .slackId("slackIdManager")
                        .role(UserEntity.Role.MANAGER)
                        .build();
                userRepository.save(manager);
            }
        };
    }
}
