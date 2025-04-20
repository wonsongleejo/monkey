package com.monkey.storereservationservice.infrastructure.client;

import com.monkey.storereservationservice.infrastructure.dto.request.ReqSlackStoreReservationPostDTOApiV1;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "slack-service", url = "${slack-service.url}")
public interface SlackFeignClient {

    @PostMapping("/v1/slacks")
    void notifySlack(@RequestBody ReqSlackStoreReservationPostDTOApiV1 request);
}