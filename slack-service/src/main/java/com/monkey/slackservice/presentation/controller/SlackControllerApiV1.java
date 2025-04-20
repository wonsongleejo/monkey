package com.monkey.slackservice.presentation.controller;

import com.monkey.common_module.dto.ResDTO;
import com.monkey.slackservice.application.dto.request.ReqSlackStoreReservationPostDTOApiV1;
import com.monkey.slackservice.application.dto.response.ResSlackStoreReservationPostDTOApiV1;
import com.monkey.slackservice.application.service.SlackService;
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

    @PostMapping
    public ResponseEntity<ResDTO<ResSlackStoreReservationPostDTOApiV1>> postBy(
            @RequestBody @Valid ReqSlackStoreReservationPostDTOApiV1 request
    ) {
        ResSlackStoreReservationPostDTOApiV1.StoreReservation reservation =
                ResSlackStoreReservationPostDTOApiV1.StoreReservation.builder()
                        .storeReservationId(UUID.randomUUID())
                        .status("SCHEDULED")
                        .timeSlot(
                                ResSlackStoreReservationPostDTOApiV1.StoreReservation.TimeSlot.builder()
                                        .store(
                                                ResSlackStoreReservationPostDTOApiV1.StoreReservation.TimeSlot.Store.builder()
                                                        .storeId(UUID.randomUUID())
                                                        .build()
                                        )
                                        .date("2025-04-19")
                                        .entryTime("10:00:00")
                                        .exitTime("11:00:00")
                                        .build()
                        )
                        .user(
                                ResSlackStoreReservationPostDTOApiV1.StoreReservation.User.builder()
                                        .userId(1L)
                                        .userName("테스트유저")
                                        .build()
                        )
                        .build();

        ResSlackStoreReservationPostDTOApiV1 resDto = slackService.notifySlackAndSave(request, reservation);

        return new ResponseEntity<>(
                ResDTO.success(resDto),
                HttpStatus.OK
        );
    }
}
