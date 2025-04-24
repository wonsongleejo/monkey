package com.monkey.userservice.presentation.controller;

import com.monkey.common_module.dto.ResDTO;
import com.monkey.userservice.application.dto.request.ReqUserPutDTOApiV1;
import com.monkey.userservice.application.dto.response.ResUserGetByIdDTOApiV1;
import com.monkey.userservice.application.dto.response.ResUserGetDTOApiV1;
import com.monkey.userservice.application.dto.response.ResUserPutDTOApiV1;
import com.monkey.userservice.application.service.UserServiceApiV1;
import com.monkey.userservice.domain.entity.UserEntity;
import com.monkey.userservice.infrastructure.client.ProductReservationFeignClientApiV1;
import com.monkey.userservice.infrastructure.client.StoreReservationFeignClientApiV1;
import com.monkey.userservice.infrastructure.client.dto.ResProductReservationClientGetDTOApiV1;
import com.monkey.userservice.infrastructure.client.dto.ResStoreReservationClientGetDTOApiV1;
import com.querydsl.core.types.Predicate;
import jakarta.ws.rs.ForbiddenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserControllerApiV1 {

    private final UserServiceApiV1 userService;
    private final StoreReservationFeignClientApiV1 storeReservationFeignClient;
    private final ProductReservationFeignClientApiV1 productReservationFeignClient;

    //사용자 전체 조회
    @GetMapping
    public ResponseEntity<ResDTO<ResUserGetDTOApiV1>> getBy(
            @QuerydslPredicate(root = UserEntity.class) Predicate predicate,
            @PageableDefault(sort="userId", size=10, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<UserEntity> userListPage = userService.getBy(predicate, pageable);

        return new ResponseEntity<>(
                ResDTO.success(ResUserGetDTOApiV1.of(userListPage)),
                HttpStatus.OK
        );
    }

    //사용자 정보 조회(본인)
    @GetMapping("/details")
    public ResponseEntity<ResDTO<ResUserGetByIdDTOApiV1>> getBy(
            @RequestHeader("X-User-Id") Long userId
    ) {
        UserEntity user = userService.getMyDetails(userId);
        ResUserGetByIdDTOApiV1 resDto = ResUserGetByIdDTOApiV1.of(user);

        return new ResponseEntity<>(
                ResDTO.success(resDto),
                HttpStatus.OK
        );
    }

    //사용자 정보 조회(마스터 용)
    @GetMapping("/master/{userId}")
    public ResponseEntity<ResDTO<ResUserGetByIdDTOApiV1>> getUserBy(
            @PathVariable(name="userId") Long userId
    ) {
        UserEntity user = userService.getUserByUserId(userId);
        ResUserGetByIdDTOApiV1 resDto = ResUserGetByIdDTOApiV1.of(user);

        return new ResponseEntity<>(
                ResDTO.success(resDto),
                HttpStatus.OK
        );
    }

    //사용자 정보 수정
    @PutMapping("/{userId}")
    public ResponseEntity<ResDTO<ResUserPutDTOApiV1>> putBy(
            @PathVariable("userId") Long pathUserId,
            @RequestHeader("X-User-Id") Long headerUserId,
            @RequestBody ReqUserPutDTOApiV1 reqDto
    ) {
        if (!pathUserId.equals(headerUserId)) {
            throw new ForbiddenException("본인 정보만 수정할 수 있습니다.");
        }

        UserEntity updatedUserEntity = userService.putByUserId(pathUserId, reqDto);
        ResUserPutDTOApiV1 resDto = ResUserPutDTOApiV1.of(updatedUserEntity);

        return new ResponseEntity<>(
                ResDTO.success(resDto),
                HttpStatus.OK
        );
    }

    //회원 탈퇴
    @DeleteMapping("/delete")
    public ResponseEntity<ResDTO<Object>> deleteBy(
            @RequestHeader("X-User-Id") Long userId
    ) {
        userService.deleteBy(userId);

        return new ResponseEntity<>(
                ResDTO.success(null),
                HttpStatus.OK
        );
    }

    //팝업 스토어 예약 내역
    @GetMapping("/{userId}/store-reservation")
    public ResponseEntity<ResDTO<ResStoreReservationClientGetDTOApiV1>> getStoreReservationBy(
            @PathVariable(name="userId") Long userId,
            @PageableDefault(sort="userId", direction = Sort.Direction.DESC) Pageable pageable
    ){
        ResDTO<ResStoreReservationClientGetDTOApiV1> storeReservationList = storeReservationFeignClient.getStoreReservations(userId);

        return new ResponseEntity<>(
                ResDTO.<ResStoreReservationClientGetDTOApiV1>builder()
                        .code("000")
                        .message("성공적으로 처리되었습니다.")
                        .data(storeReservationList.getData())
                        .build(),
                HttpStatus.OK
        );
    }

    //한정 상품 예약 내역
    @GetMapping("/{userId}/product-reservation")
    public ResponseEntity<ResDTO<ResProductReservationClientGetDTOApiV1>> getProductReservationsBy(
            @PathVariable(name="userId") Long userId,
            @PageableDefault(sort="userId", direction = Sort.Direction.DESC) Pageable pageable
    ){
        ResDTO<ResProductReservationClientGetDTOApiV1> productReservationList = productReservationFeignClient.getProductReservations(userId);

        return new ResponseEntity<>(
                ResDTO.<ResProductReservationClientGetDTOApiV1>builder()
                        .code("000")
                        .message("성공적으로 처리되었습니다.")
                        .data(productReservationList.getData())
                        .build(),
                HttpStatus.OK
        );
    }
}