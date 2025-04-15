package com.monkey.userservice.infrastructure.persistence;

import com.monkey.userservice.domain.entity.UserEntity;
import com.monkey.userservice.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;
    private final UserQueryDslRepository userQueryDslRepository;

    @Override
    public UserEntity save(UserEntity userEntity) {
        userJpaRepository.save(userEntity);
        return userEntity;
    }

    @Override
    public List<UserEntity> findAllByIsDeletedFalse(Pageable pageable) {
        List<UserEntity> userList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            userList.add(UserEntity.builder()
                    .userId((long) i)
                    .username("testUser"+ i)
                    .slackId("slackID" + i)
                    .role(UserEntity.Role.USER)
                    .build()
            );
        }
        //return userJpaRepository.findAllByIsDeletedFalse(pageable);
        return userList;
    }

    @Override
    public Optional<UserEntity> findByIsDeletedFalse(Long userId) {

        return userJpaRepository.findById(userId);
    }
}