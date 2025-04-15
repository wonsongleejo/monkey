package com.monkey.userservice.domain.repository;

import com.monkey.userservice.domain.entity.UserEntity;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserRepository {

    UserEntity save(UserEntity userEntity);

    Page<UserEntity> findAllByIsDeletedFalse(Predicate predicate, Pageable pageable);

    Optional<UserEntity> findByIsDeletedFalse(Long userId);
}