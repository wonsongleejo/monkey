package com.monkey.storeservice.presentation;

import com.monkey.commonmodule.dto.ResDTO;
import com.monkey.storeservice.application.dto.request.ReqStorePostDtoApiV1;
import com.monkey.storeservice.application.dto.response.ResStoreGetDtoApiV1;
import com.monkey.storeservice.application.dto.response.ResStorePutDtoApiV1;
import com.monkey.storeservice.application.service.StoreServiceApi;
import com.monkey.storeservice.application.dto.response.ResStorePostDtoApiV1;
import com.monkey.storeservice.domain.article.entity.StoreEntity;
import com.monkey.storeservice.domain.article.entity.StoreEntity.OpenStatus;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
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
        .endTime(LocalTime.parse("18:00"))
        .openStatus(OpenStatus.CLOSED)
        .build();
    ResStorePostDtoApiV1 resDto = ResStorePostDtoApiV1.of(storeEntity.getStoreId());

    return new ResponseEntity<>(
        ResDTO.success(resDto),
        HttpStatus.OK
    );

  }

  // 팝업스토어 수정
  @PutMapping("/{storeId}")
  public ResponseEntity<ResDTO<ResStorePutDtoApiV1>> putById(
      @PathVariable UUID storeId,
      @RequestBody ReqStorePostDtoApiV1 reqStorePostDtoApiV1) {
    StoreEntity storeEntity = StoreEntity.builder()
        .storeName("오징어게임 팝업")
        .description("오징어게임 캐릭터가 있어요")
        .startDate(LocalDate.parse("2025-04-10"))
        .endDate(LocalDate.parse("2025-04-17"))
        .startTime(LocalTime.parse("10:00"))
        .endTime(LocalTime.parse("18:00"))
        .openStatus(OpenStatus.OPEN)
        .totalPerson(100)
        .build();

    ResStorePutDtoApiV1 resDto = ResStorePutDtoApiV1.of(storeEntity);

    return new ResponseEntity<>(
        ResDTO.success(resDto),
        HttpStatus.OK
    );
  }

  // 팝업스토어 조회
  @GetMapping("/{storeId}")
  public ResponseEntity<ResDTO<ResStoreGetDtoApiV1>> getById(@PathVariable UUID storeId) {

    // 더미 데이터
    StoreEntity storeEntity = StoreEntity.builder()
        .storeId(storeId)
        .storeName("루피 팝업스토어")
        .description("귀여운 굿즈가 가득해요")
        .startDate(LocalDate.parse("2025-04-09"))
        .endDate(LocalDate.parse("2025-05-09"))
        .startTime(LocalTime.parse("10:00"))
        .endTime(LocalTime.parse("18:00"))
        .totalPerson(100)
        .openStatus(StoreEntity.OpenStatus.OPEN)
        .build();

    // DTO 변환
    ResStoreGetDtoApiV1 resDto = ResStoreGetDtoApiV1.of(storeEntity);

    return new ResponseEntity<>(
        ResDTO.success(resDto),
        HttpStatus.OK
    );
  }
  // 팝업스토어 전체 조회
  @GetMapping
  public ResponseEntity<ResDTO<List<ResStoreGetDtoApiV1>>>getBy() {
    List<StoreEntity> storeList = new ArrayList<>();

    for (int i = 0; i < 5; i++) {
      storeList.add(StoreEntity.builder()
          .storeId(UUID.randomUUID())
          .storeName("루피 팝업스토어 " + i)
          .description("귀여운 굿즈가 가득한 팝업 " + i)
          .startDate(LocalDate.of(2025, 4, 9))
          .endDate(LocalDate.of(2025, 4, 16))
          .startTime(LocalTime.of(10, 0))
          .endTime(LocalTime.of(18, 0))
          .totalPerson(100 + i * 10)
          .openStatus(StoreEntity.OpenStatus.OPEN)
          .build()
      );
    }
    List<ResStoreGetDtoApiV1> resDto = ResStoreGetDtoApiV1.of(storeList);

    return new ResponseEntity<>(
        ResDTO.success(resDto),
        HttpStatus.OK
    );
  }
}
