package com.monkey.userservice.infrastructure.persistence;

import com.monkey.userservice.domain.entity.QUserEntity;
import com.monkey.userservice.domain.entity.UserEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserQueryDslRepositoryImpl implements UserQueryDslRepository{

    private final JPAQueryFactory jpaQueryFactory;

    private static final int DEFAULT_SIZE = 10;
    private static final List<Integer> ALLOWED_PAGE_SIZES = Arrays.asList(10, 30, 50);

    @Override
    public Page<UserEntity> findAllByIsDeletedFalse(Predicate predicate, Pageable pageable) {
        int pageSize = validatePageSize(pageable.getPageSize());

        Pageable validatedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageSize,
                pageable.getSort()
        );

        QUserEntity qUserEntity = QUserEntity.userEntity;

        BooleanBuilder baseCondition = new BooleanBuilder();
        baseCondition.and(qUserEntity.isDeleted.isFalse()); // 탈퇴하지 않은 사용자만

        if(predicate != null){
            baseCondition.and(predicate);
        }

        List<UserEntity> content = jpaQueryFactory
                .selectFrom(qUserEntity)
                .where(baseCondition)
                .offset(validatedPageable.getOffset())
                .limit(validatedPageable.getPageSize())
                .orderBy(getOrderSpecifier(validatedPageable.getSort()).toArray(OrderSpecifier[]::new))
                .fetch();

        Long total = jpaQueryFactory
                .select(qUserEntity.count())
                .from(qUserEntity)
                .where(baseCondition)
                .fetchOne();

        return new PageImpl<>(content, validatedPageable, Optional.ofNullable(total).orElse(0L));
    }

    //정렬기준 생성
    @SuppressWarnings("unchecked")
    public static OrderSpecifier<?> getSortedColumn(Order order, Path<?> parent, String fieldName) {
        Path<Object> fieldPath = Expressions.path(Object.class, parent, fieldName);
        return new OrderSpecifier(order, fieldPath);
    }

    private static List<OrderSpecifier<?>> getOrderSpecifier(Sort sort) {
        return sort.stream()
                .map(order -> {
                    Order direction = order.isAscending() ? Order.ASC : Order.DESC;
                    return getSortedColumn(direction, QUserEntity.userEntity, order.getProperty());
                })
                .collect(Collectors.toList());
    }

    //페이지 사이즈 검증
    private int validatePageSize(int requestedSize) {
        return ALLOWED_PAGE_SIZES.contains(requestedSize) ? requestedSize : DEFAULT_SIZE;
    }
}