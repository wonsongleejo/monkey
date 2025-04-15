package com.monkey.userservice.domain.repository;

import com.monkey.userservice.domain.entity.UserEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    UserEntity save(UserEntity userEntity);

    List<UserEntity> findAllByIsDeletedFalse(Pageable pageable);

    Optional<UserEntity> findByIsDeletedFalse(Long userId);
}