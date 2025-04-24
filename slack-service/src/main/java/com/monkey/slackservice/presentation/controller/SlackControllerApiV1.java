package com.monkey.slackservice.presentation.controller;

import com.monkey.common_module.dto.ResDTO;
import com.monkey.common_module.dto.ResponseCode;
import com.monkey.slackservice.application.dto.request.ReqSlackStoreReservationPostDTOApiV1;
import com.monkey.slackservice.application.dto.response.ResSlackStoreReservationPostDTOApiV1;
import com.monkey.slackservice.application.dto.response.ResSlackProductReservationPostDTOApiV1;
import com.monkey.slackservice.application.service.SlackService;
import com.monkey.slackservice.domain.slack.vo.SlackMessageType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/slacks")
@RequiredArgsConstructor
public class SlackControllerApiV1 {

    private final SlackService slackService;

    private static final UUID FIXED_STORE_RESERVATION_ID = UUID.fromString("502fbab8-4b9a-42ff-ba66-e8f993d1c06a");
    private static final UUID FIXED_STORE_ID = UUID.fromString("fe3e1077-e161-4f21-8025-4798bd43da87");
    private static final UUID FIXED_PRODUCT_RESERVATION_ID = UUID.fromString("cafebabe-1234-5678-90ab-abcdefabcdef");
    private static final UUID FIXED_PRODUCT_ID = UUID.fromString("deadbeef-dead-beef-dead-beefdeadbeef");

    @PostMapping
    public ResponseEntity<ResDTO<?>> sendSlackMessage(
            @Valid @RequestBody ReqSlackStoreReservationPostDTOApiV1 request
    ) {
        SlackMessageType type = request.getSlack().getSlackMessageType();

        if (type.name().startsWith("STORE_")) {
            ResSlackStoreReservationPostDTOApiV1.StoreReservation reservation = ResSlackStoreReservationPostDTOApiV1.StoreReservation.builder()
                    .storeReservationId(FIXED_STORE_RESERVATION_ID)
                    .status("SCHEDULED")
                    .timeSlot(ResSlackStoreReservationPostDTOApiV1.StoreReservation.TimeSlot.builder()
                            .store(ResSlackStoreReservationPostDTOApiV1.StoreReservation.TimeSlot.Store.builder()
                                    .storeId(FIXED_STORE_ID)
                                    .build())
                            .date("2025-04-19")
                            .entryTime("14:00:00")
                            .exitTime("15:00:00")
                            .build())
                    .user(ResSlackStoreReservationPostDTOApiV1.StoreReservation.User.builder()
                            .userId(1L)
                            .userName("testuser")
                            .build())
                    .build();

            return new ResponseEntity<>(
                    ResDTO.success(slackService.notifyStoreReservationSlackAndSave(request, reservation)),
                    HttpStatus.OK
            );
        } else if (type.name().startsWith("PRODUCT_")) {
            ResSlackProductReservationPostDTOApiV1.ProductReservation reservation = ResSlackProductReservationPostDTOApiV1.ProductReservation.builder()
                    .productReservationId(FIXED_PRODUCT_RESERVATION_ID)
                    .product(ResSlackProductReservationPostDTOApiV1.ProductReservation.Product.builder()
                            .productId(FIXED_PRODUCT_ID)
                            .productName("Dummy Product")
                            .build())
                    .store(ResSlackProductReservationPostDTOApiV1.ProductReservation.Store.builder()
                            .storeId(FIXED_STORE_ID)
                            .storeName("Dummy Store")
                            .build())
                    .user(ResSlackProductReservationPostDTOApiV1.ProductReservation.User.builder()
                            .userId(1L)
                            .userName("testuser")
                            .build())
                    .quantity(1)
                    .status("PENDING_PICKUP")
                    .build();

            return new ResponseEntity<>(
                    ResDTO.success(slackService.notifyProductReservationSlackAndSave(request, reservation)),
                    HttpStatus.OK
            );
        }

        return new ResponseEntity<>(
                ResDTO.fail(ResponseCode.SLACK_SEND_FAILED),
                HttpStatus.BAD_REQUEST
        );
    }
}