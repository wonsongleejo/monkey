package com.monkey.userservice.infrastructure.persistence;

import com.monkey.userservice.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long>, QuerydslPredicateExecutor<UserEntity> {

}
