package com.monkey.slackservice.presentation.controller;

import com.monkey.common_module.dto.ResDTO;
import com.monkey.slackservice.application.dto.request.ReqSlackStoreMessageDTOApiV1;
import com.monkey.slackservice.application.dto.response.ResSlackMessageDTOApiV1;
import com.monkey.slackservice.application.service.SlackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/slacks")
@RequiredArgsConstructor
public class SlackControllerApiV1 {

    private final SlackService slackService;

    @PostMapping("/store")
    public ResponseEntity<ResDTO<ResSlackMessageDTOApiV1>> sendStoreSlackMessage(
            @RequestBody @Valid ReqSlackStoreMessageDTOApiV1 request
    ) {
        ResSlackMessageDTOApiV1 res = slackService.sendStoreReservationMessage(request);
        return ResponseEntity.ok(ResDTO.success(res));
    }
}