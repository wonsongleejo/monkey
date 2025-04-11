package com.monkey.slackservice.presentation.controller;

import com.monkey.commonmodule.dto.ResDTO;
import com.monkey.slackservice.application.dto.request.ReqSlackStoreReservationPostDTOApiV1;
import com.monkey.slackservice.application.dto.response.ResSlackStoreReservationPostDTOApiV1;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/slacks")
@RequiredArgsConstructor
public class SlackControllerApiV1 {

    @PostMapping
    public ResponseEntity<ResDTO<ResSlackStoreReservationPostDTOApiV1>> postBy(
            @RequestBody @Valid ReqSlackStoreReservationPostDTOApiV1 request
    ) {
        ResSlackStoreReservationPostDTOApiV1 resDto = ResSlackStoreReservationPostDTOApiV1.of(
                request.getSlack().getSlackId(),
                "SCHEDULED"
        );

        return new ResponseEntity<>(
                ResDTO.success(resDto),
                HttpStatus.OK
        );
    }
}