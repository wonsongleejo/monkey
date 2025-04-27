package com.monkey.storereservationservice.infrastructure.client;

import com.monkey.storereservationservice.infrastructure.dto.response.ResStoreTimeSlotDTOApiV1;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "store-service", url = "${store-service.url}", primary = false)
public interface StoreClient {

    @GetMapping("/v1/stores/timeslots/{timeSlotId}")
    ResStoreTimeSlotDTOApiV1 getTimeSlotById(@PathVariable("timeSlotId") UUID timeSlotId);
}