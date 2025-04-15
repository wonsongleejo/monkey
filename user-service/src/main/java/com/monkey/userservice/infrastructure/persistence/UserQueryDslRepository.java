package com.monkey.userservice.infrastructure.persistence;

import com.monkey.userservice.domain.entity.UserEntity;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserQueryDslRepository {
    Page<UserEntity> findAllByIsDeletedFalse(Predicate predicate, Pageable pageable);
}
