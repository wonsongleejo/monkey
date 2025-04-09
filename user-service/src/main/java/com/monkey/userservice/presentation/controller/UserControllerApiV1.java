package com.monkey.userservice.presentation.controller;

import com.monkey.commonmodule.dto.ResDTO;
import com.monkey.userservice.application.dto.request.ReqUserPutDTOApiV1;
import com.monkey.userservice.application.dto.response.ResUserGetByIdDTOApiV1;
import com.monkey.userservice.application.dto.response.ResUserGetDTOApiV1;
import com.monkey.userservice.application.dto.response.ResUserPutDTOApiV1;
import com.monkey.userservice.domain.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserControllerApiV1 {

    //사용자 전체 조회
    @GetMapping
    public ResponseEntity<ResDTO<ResUserGetDTOApiV1>> getBy(
            //@RequestParam(required = false) String searchValue,
            //@PageableDefault(sort="id", direction = Sort.Direction.DESC) Pageable pageable
    ) {

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

        ResUserGetDTOApiV1 response = ResUserGetDTOApiV1.of(userList);

        return new ResponseEntity<>(
                ResDTO.success(response),
                HttpStatus.OK
        );
    }

    //사용자 정보 조회
    @GetMapping("/{userId}")
    public ResponseEntity<ResDTO<ResUserGetByIdDTOApiV1>> getBy(@PathVariable(name="userId") Long userId) {

        UserEntity user = UserEntity.builder()
                .userId(userId)
                .username("testUser")
                .slackId("slackId1")
                .role(UserEntity.Role.USER)
                .build();

        ResUserGetByIdDTOApiV1 resDto = ResUserGetByIdDTOApiV1.of(user);

        return new ResponseEntity<>(
                ResDTO.success(resDto),
                HttpStatus.OK
        );
    }

    //사용자 정보 수정
    @PutMapping("/{userId}")
    public ResponseEntity<ResDTO<ResUserPutDTOApiV1>> putBy(
            @PathVariable(name="userId") Long userId, @RequestBody ReqUserPutDTOApiV1 reqDto
    ) {

        UserEntity user = UserEntity.builder()
                .userId(userId)
                .password(reqDto.getUser().getPassword())
                .slackId(reqDto.getUser().getSlackId())
                .build();

        ResUserPutDTOApiV1 resDto = ResUserPutDTOApiV1.of(user);

        return new ResponseEntity<>(
                ResDTO.success(resDto),
                HttpStatus.OK
        );
    }

    //회원 탈퇴
    @DeleteMapping("/{userId}")
    public ResponseEntity<ResDTO<Object>> deleteBy(@PathVariable(name="userId") Long userId) {

        //soft-delete : 추후 service 제작 시 적용
        UserEntity userEntity = UserEntity.builder()
                .isDeleted(true)
                .build();

        return new ResponseEntity<>(
                ResDTO.success(),
                HttpStatus.OK
        );
    }
}
