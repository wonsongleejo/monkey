package com.monkey.userservice.infrastructure.persistence;

import com.monkey.userservice.domain.entity.UserEntity;
import com.monkey.userservice.domain.repository.UserRepository;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
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

    public Page<UserEntity> findAllByIsDeletedFalse(Predicate predicate, Pageable pageable) {
        //return userJpaRepository.findAll(predicate, pageable);
        return userQueryDslRepository.findAllByIsDeletedFalse(predicate, pageable);
    }

    @Override
    public Optional<UserEntity> findByIsDeletedFalse(Long userId) {
        return userJpaRepository.findById(userId);
    }

    @Override
    public long count() {
        return userJpaRepository.count();
    }
}