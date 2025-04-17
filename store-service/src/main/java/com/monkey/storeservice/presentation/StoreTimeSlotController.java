package com.monkey.storeservice.presentation;

import com.monkey.commonmodule.dto.ResDTO;
import com.monkey.storeservice.application.dto.request.ReqStoreTimeSlotPostDtoApiV1;
import com.monkey.storeservice.application.dto.request.ReqStoreTimeSlotPutDtoApiV1;
import com.monkey.storeservice.application.dto.response.ResStoreTimeSlotGetDtoApiV1;
import com.monkey.storeservice.application.dto.response.ResStoreTimeSlotPostDtoApiV1;
import com.monkey.storeservice.application.dto.response.ResStoreTimeSlotPutDtoApiV1;
import com.monkey.storeservice.domain.article.entity.StoreTimeSlotEntity;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/timeslots")
@RequiredArgsConstructor
public class StoreTimeSlotController {

  //팝업스토어 시간대 생성
  @PostMapping("/{storeId}")
  public ResponseEntity<ResDTO<ResStoreTimeSlotPostDtoApiV1>> postBy(@RequestBody
      ReqStoreTimeSlotPostDtoApiV1 reqStoreTimeSlotPostDtoApiV1) {
    StoreTimeSlotEntity storeTimeSlotEntity = StoreTimeSlotEntity.builder()
        .timeSlotId(UUID.randomUUID())
        .slotDate(LocalDate.parse("2025-04-14"))
        .entryTime(LocalTime.parse("00:00:00"))
        .exitTime(LocalTime.parse("00:00:00"))
        .maxPerson(10)
        .build();

//    // DB사용시에 저장하기 위한 메서드
//    StoreTimeSlotEntity storeTimeSlotEntity1 = reqStoreTimeSlotPostDtoApiV1.getStoreTimeSlot().toStoreTimeSlotEntity();

    ResStoreTimeSlotPostDtoApiV1 resDto = ResStoreTimeSlotPostDtoApiV1.of(storeTimeSlotEntity);

    return new ResponseEntity<>(
        ResDTO.success(resDto),
        HttpStatus.OK
    );
  }

  //팝업스토어 시간대 수정
  @PutMapping("/{timeSlotId}")
  public ResponseEntity<ResDTO<ResStoreTimeSlotPutDtoApiV1>> putById(
      @PathVariable UUID timeSlotId,
      @RequestBody ReqStoreTimeSlotPutDtoApiV1 reqStoreTimeSlotPutDtoApiV1) {

    StoreTimeSlotEntity storeTimeSlotEntity = StoreTimeSlotEntity.builder()
        .slotDate(LocalDate.parse("2025-04-14"))
        .entryTime(LocalTime.parse("00:00:00"))
        .exitTime(LocalTime.parse("00:00:00"))
        .maxPerson(20)
        .build();

    //db에 저장하기위한 update 메서드
    reqStoreTimeSlotPutDtoApiV1.getStoreTimeSlot().update(storeTimeSlotEntity);

    ResStoreTimeSlotPutDtoApiV1 respDto = ResStoreTimeSlotPutDtoApiV1.of(storeTimeSlotEntity);

    return new ResponseEntity<>(
        ResDTO.success(respDto),
        HttpStatus.OK
    );
  }

  //팝업스토어 시간대 조회
  @GetMapping("/{timeSlotId}")
  public ResponseEntity<ResDTO<ResStoreTimeSlotGetDtoApiV1>> getById(
      @PathVariable UUID timeSlotId){
    StoreTimeSlotEntity storeTimeSlotEntity = StoreTimeSlotEntity.builder()
        .storeId(UUID.randomUUID())
        .timeSlotId(timeSlotId)
        .slotDate(LocalDate.parse("2025-04-14"))
        .entryTime(LocalTime.parse("00:00:00"))
        .exitTime(LocalTime.parse("00:00:00"))
        .maxPerson(20)
        .build();

    ResStoreTimeSlotGetDtoApiV1 resDto = ResStoreTimeSlotGetDtoApiV1.of(storeTimeSlotEntity);

    return new ResponseEntity<>(
        ResDTO.success(resDto),
        HttpStatus.OK
    );
  }

  //팝업스토어 시간대 전체 조회
  @GetMapping
  public ResponseEntity<ResDTO<ResStoreTimeSlotGetDtoApiV1>> getBy() {

    List<StoreTimeSlotEntity> storeTimeSlotEntityList = new ArrayList<>();

    for (int i = 0; i < 5; i++) {
      storeTimeSlotEntityList.add(StoreTimeSlotEntity.builder()
          .storeId(UUID.randomUUID())
          .timeSlotId(UUID.randomUUID())
          .slotDate(LocalDate.parse("2025-04-14"))
          .entryTime(LocalTime.parse("00:00:00"))
          .exitTime(LocalTime.parse("00:00:00"))
          .maxPerson(20)
          .build()
      );
    }
    ResStoreTimeSlotGetDtoApiV1 resDto = ResStoreTimeSlotGetDtoApiV1.of(storeTimeSlotEntityList);

    return new ResponseEntity<>(
          ResDTO.success(resDto),
          HttpStatus.OK
      );
    }
}
