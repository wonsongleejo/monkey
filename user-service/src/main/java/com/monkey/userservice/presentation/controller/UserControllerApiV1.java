package com.monkey.userservice.presentation.controller;

import com.monkey.commonmodule.dto.ResDTO;
import com.monkey.userservice.application.dto.request.ReqUserPutDTOApiV1;
import com.monkey.userservice.application.dto.response.*;
import com.monkey.userservice.domain.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    //팝업 스토어 예약 내역
    @GetMapping("/{userId}/store-reservation")
    public ResponseEntity<ResDTO<ResStoreReservationGetDTOApiV1>> getStoreReservationBy(
            @PathVariable(name="userId") Long userId
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
            //최상단 데이터 조합
            ResStoreReservationClientGetDTOApiV1.ModelData.StoreReservation reservation =
                    ResStoreReservationClientGetDTOApiV1.ModelData.StoreReservation.builder()
                            .userId(userId)
                            .storeReservationId(UUID.randomUUID())
                            .visitStatus("SCHEDULED")
                            .timeSlot(timeSlot)
                            .build();

            storeReservationList.add(reservation);
        }

        return new ResponseEntity<>(
                ResDTO.<ResStoreReservationGetDTOApiV1>builder()
                        .code("000")
                        .message("성공적으로 처리되었습니다.")
                        .data(ResStoreReservationGetDTOApiV1.of(storeReservationList))
                        .build(),
                HttpStatus.OK
        );
    }

    //한정 상품 예약 내역
    @GetMapping("/{userId}/product-reservation")
    public ResponseEntity<ResDTO<ResProductReservationGetDTOApiV1>> getProductReservationsBy(
            @PathVariable(name="userId") Long userId
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

        return new ResponseEntity<>(
                ResDTO.<ResProductReservationGetDTOApiV1>builder()
                        .code("000")
                        .message("성공적으로 처리되었습니다.")
                        .data(ResProductReservationGetDTOApiV1.of(prodcutReservationList))
                        .build(),
                HttpStatus.OK
        );
    }
}
