package com.monkey.userservice.application.service;

import com.monkey.userservice.application.dto.request.ReqUserPutDTOApiV1;
import com.monkey.userservice.domain.entity.UserEntity;
import com.monkey.userservice.domain.repository.UserRepository;
import com.monkey.userservice.infrastructure.client.StoreReservationFeignClientApiV1;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImplApiV1 implements UserServiceApiV1 {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final StoreReservationFeignClientApiV1 storeReservationFeignClientApiV1;

    @Override
    public Page<UserEntity> getBy(Predicate predicate, Pageable pageable) {
        return userRepository.findAllByIsDeletedFalse(predicate, pageable);
    }

    @Override
    public UserEntity getByUserId(Long userId) {

        return userRepository.findByIsDeletedFalse(userId)
                .orElseThrow(()-> new IllegalArgumentException("회원이 존재하지 않습니다."));
    }

    @Override
    @Transactional
    public UserEntity putByUserId(Long userId, ReqUserPutDTOApiV1 reqDto) {
        UserEntity user = userRepository.findByIsDeletedFalse(userId)
                .orElseThrow(()-> new IllegalArgumentException("회원이 존재하지 않습니다."));

        // 비밀번호 암호화
        String encryptedPassword = passwordEncoder.encode(reqDto.getUser().getPassword());
        user.update(encryptedPassword, reqDto.getUser().getSlackId());

        return user;
    }

    @Override
    @Transactional
    public void deleteBy(Long userId) {
        UserEntity user = userRepository.findByIsDeletedFalse(userId)
                .orElseThrow(()-> new IllegalArgumentException("회원이 존재하지 않습니다."));

        user.setDeleted(true);
    }
}
