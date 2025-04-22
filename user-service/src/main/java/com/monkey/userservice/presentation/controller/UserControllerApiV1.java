package com.monkey.userservice.presentation.controller;

import com.monkey.common_module.dto.ResDTO;
import com.monkey.userservice.application.dto.request.ReqUserPutDTOApiV1;
import com.monkey.userservice.application.dto.response.*;
import com.monkey.userservice.application.service.UserServiceApiV1;
import com.monkey.userservice.domain.entity.UserEntity;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserControllerApiV1 {

    private final UserServiceApiV1 userService;

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

    //사용자 정보 조회
    @GetMapping("/details")
    public ResponseEntity<ResDTO<ResUserGetByIdDTOApiV1>> getBy(
            @RequestHeader("X-User-Id") Long userId
    ) {
        UserEntity user = userService.getByUserId(userId);
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
        UserEntity user = userService.getByUserId(userId);
        ResUserGetByIdDTOApiV1 resDto = ResUserGetByIdDTOApiV1.of(user);

        return new ResponseEntity<>(
                ResDTO.success(resDto),
                HttpStatus.OK
        );
    }

    //사용자 정보 수정
    @PutMapping("/{userId}")
    public ResponseEntity<ResDTO<ResUserPutDTOApiV1>> putBy(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody ReqUserPutDTOApiV1 reqDto
    ) {

        UserEntity updatedUserEntity = userService.putByUserId(userId, reqDto);
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
            //@PathVariable(name="userId") Long userId
    ) {
        userService.deleteBy(userId);

        return new ResponseEntity<>(
                ResDTO.success(null),
                HttpStatus.OK
        );
    }

    //팝업 스토어 예약 내역
    @GetMapping("/{userId}/store-reservation")
    public ResponseEntity<ResDTO<ResStoreReservationGetDTOApiV1>> getStoreReservationBy(
            @PathVariable(name="userId") Long userId,
            @PageableDefault(sort="userId", direction = Sort.Direction.DESC) Pageable pageable
    ){

        //List<ResStoreReservationGetApiDTOV1> reservations = ProductReservationFeignClient.getStoreReservations();
        List<ResStoreReservationClientGetDTOApiV1.ModelData.StoreReservation> storeReservationList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            //store 데이터
            ResStoreReservationClientGetDTOApiV1.ModelData.StoreReservation.TimeSlot.Store store=
                    ResStoreReservationClientGetDTOApiV1.ModelData.StoreReservation.TimeSlot.Store.builder()
                            .storeId(UUID.randomUUID())
                            .build();

            //timeslot 데이터
            ResStoreReservationClientGetDTOApiV1.ModelData.StoreReservation.TimeSlot timeSlot =
                    ResStoreReservationClientGetDTOApiV1.ModelData.StoreReservation.TimeSlot.builder()
                            .store(store)
                            .date(LocalDate.now())
                            .entryTime(LocalTime.of(12+i,0,0))
                            .exitTime(LocalTime.of(13+i,0,0))
                            .build();
            ResStoreReservationClientGetDTOApiV1.ModelData.StoreReservation.User User =
                    ResStoreReservationClientGetDTOApiV1.ModelData.StoreReservation.User.builder()
                            .userId((long) i)
                            .userName(UUID.randomUUID().toString())
                            .build();
            //최상단 데이터 조합
            ResStoreReservationClientGetDTOApiV1.ModelData.StoreReservation reservation =
                    ResStoreReservationClientGetDTOApiV1.ModelData.StoreReservation.builder()
                            .user(User)
                            .storeReservationId(UUID.randomUUID())
                            .visitStatus("SCHEDULED")
                            .timeSlot(timeSlot)
                            .build();

            storeReservationList.add(reservation);
        }

        Page<ResStoreReservationClientGetDTOApiV1.ModelData.StoreReservation> sotreReservationListPage =
                new PageImpl<>(storeReservationList, pageable, storeReservationList.size());

        return new ResponseEntity<>(
                ResDTO.<ResStoreReservationGetDTOApiV1>builder()
                        .code("000")
                        .message("성공적으로 처리되었습니다.")
                        .data(ResStoreReservationGetDTOApiV1.of(sotreReservationListPage))
                        .build(),
                HttpStatus.OK
        );
    }

    //한정 상품 예약 내역
    @GetMapping("/{userId}/product-reservation")
    public ResponseEntity<ResDTO<ResProductReservationGetDTOApiV1>> getProductReservationsBy(
            @PathVariable(name="userId") Long userId,
            @PageableDefault(sort="userId", direction = Sort.Direction.DESC) Pageable pageable
    ){
        //List<ResProductReservationClientGetDTOApiV1> reservations = ProductReservationFeignClient.getStoreReservations();
        List<ResProductReservationClientGetDTOApiV1.ModelData.ProductReservation> prodcutReservationList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            ResProductReservationClientGetDTOApiV1.ModelData.ProductReservation.Product product =
                    ResProductReservationClientGetDTOApiV1.ModelData.ProductReservation.Product.builder()
                            .productId(UUID.randomUUID())
                            .productName("맛있는 마카롱 " + i)
                            .build();

            ResProductReservationClientGetDTOApiV1.ModelData.ProductReservation.Store store =
                    ResProductReservationClientGetDTOApiV1.ModelData.ProductReservation.Store.builder()
                            .storeId(UUID.randomUUID())
                            .storeName("마카롱 맛집 " + i)
                            .quantity(i + 1)
                            .receivedStatus("PENDING")
                            .build();

            ResProductReservationClientGetDTOApiV1.ModelData.ProductReservation reqDto =
                    ResProductReservationClientGetDTOApiV1.ModelData.ProductReservation.builder()
                            .productReservationId(UUID.randomUUID())
                            .product(product)
                            .store(store)
                            .build();

            prodcutReservationList.add(reqDto);
        }

        Page<ResProductReservationClientGetDTOApiV1.ModelData.ProductReservation> productReservationListPage =
                new PageImpl<>(prodcutReservationList, pageable, prodcutReservationList.size());

        return new ResponseEntity<>(
                ResDTO.<ResProductReservationGetDTOApiV1>builder()
                        .code("000")
                        .message("성공적으로 처리되었습니다.")
                        .data(ResProductReservationGetDTOApiV1.of(productReservationListPage))
                        .build(),
                HttpStatus.OK
        );
    }
}