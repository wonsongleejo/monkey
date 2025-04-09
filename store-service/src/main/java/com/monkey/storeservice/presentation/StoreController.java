package com.monkey.storeservice.presentation;

import com.monkey.commonmodule.dto.ResDTO;
import com.monkey.storeservice.application.dto.request.ReqStorePostDtoApiV1;
import com.monkey.storeservice.application.dto.response.ResStorePutDtoApiV1;
import com.monkey.storeservice.application.service.StoreServiceApi;
import com.monkey.storeservice.application.dto.response.ResStorePostDtoApiV1;
import com.monkey.storeservice.domain.article.entity.StoreEntity;
import com.monkey.storeservice.domain.article.entity.StoreEntity.OpenStatus;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/stores")
@RequiredArgsConstructor
public class StoreController {

  StoreServiceApi storeServiceApi;

  //  팝업스토어 생성
  @PostMapping
  public ResponseEntity<ResDTO<ResStorePostDtoApiV1>> postBy(@RequestBody ReqStorePostDtoApiV1 reqStorePostDtoApiV1) {
    StoreEntity storeEntity = StoreEntity.builder()
        .storeId(UUID.randomUUID())
        .storeName("루피팝업")
        .description("다양한 루피")
        .startDate(LocalDate.parse("2025-04-09"))
        .endDate(LocalDate.parse("2025-04-16"))
        .startTime(LocalTime.parse("10:00"))
        .endTime(LocalTime.parse("`18:00"))
        .openStatus(OpenStatus.CLOSED)
        .build();
    ResStorePostDtoApiV1 response = ResStorePostDtoApiV1.of(storeEntity.getStoreId());

    return new ResponseEntity<>(
        ResDTO.success(response),
        HttpStatus.OK
    );

  }

//  // 팝업스토어 수정
//  @PutMapping("/{storeId}")
//  public ResponseEntity<ResDTO<ResStorePutDtoApiV1>> putById(
//      @PathVariable UUID storeId,
//      @RequestBody ReqStorePostDtoApiV1 reqStorePostDtoApiV1) {
//    StoreEntity storeEntity = StoreEntity.builder()
//        .storeId(UUID.randomUUID())
//        .storeName("오징어게임 팝업")
//        .description("오징어게임 캐릭터가 있어요")
//        .startDate(LocalDate.parse("2025-04-10"))
//        .endDate(LocalDate.parse("2025-04-17"))
//        .startTime(LocalTime.parse("10:00"))
//        .endTime(LocalTime.parse("`18:00"))
//        .openStatus(OpenStatus.OPEN)
//        .build();
//
//    ResStorePutDtoApiV1 response = ResStorePutDtoApiV1.of(storeEntity);
//
//    return new ResponseEntity<>(
//        ResDTO.success(response),
//        HttpStatus.OK
//    );
//  }
}
