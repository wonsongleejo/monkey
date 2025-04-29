package com.monkey.storeservice.presentation;

import com.monkey.common_module.dto.ResDTO;
import com.monkey.storeservice.application.dto.request.ReqStoreTimeSlotPostDTOApiV1;
import com.monkey.storeservice.application.dto.request.ReqStoreTimeSlotPutDTOApiV1;
import com.monkey.storeservice.application.dto.response.ResStoreTimeSlotGetDTOApiV1;
import com.monkey.storeservice.application.dto.response.ResStoreTimeSlotPostDTOApiV1;
import com.monkey.storeservice.application.dto.response.ResStoreTimeSlotPutDTOApiV1;
import com.monkey.storeservice.application.service.v1.StoreTimeSlotServiceApiV1;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/stores")
@RequiredArgsConstructor
public class StoreTimeSlotController {

  private final StoreTimeSlotServiceApiV1 storeTimeSlotServiceApiV1;

  //  팝업스토어 시간대 생성
  @PostMapping("/{storeId}")
  public ResponseEntity<ResDTO<ResStoreTimeSlotPostDTOApiV1>> postById(
      @RequestHeader("X-User-Id") Long storeManagerId,
      @RequestHeader("X-User-Role") String role,
      @PathVariable UUID storeId,
      @RequestBody ReqStoreTimeSlotPostDTOApiV1 reqDto) {

    ResStoreTimeSlotPostDTOApiV1 resDto = storeTimeSlotServiceApiV1.postById(storeId, reqDto,storeManagerId,role);

    return new ResponseEntity<>(
        ResDTO.success(resDto),
        HttpStatus.OK
    );
  }

  //팝업스토어 시간대 수정
  @PutMapping("/{storeId}/timeslots/{timeSlotId}")
  public ResponseEntity<ResDTO<ResStoreTimeSlotPutDTOApiV1>> putById(
      @RequestHeader("X-User-Id") Long storeManagerId,
      @RequestHeader("X-User-Role") String role,
      @PathVariable UUID timeSlotId,
      @PathVariable UUID storeId,
      @RequestBody ReqStoreTimeSlotPutDTOApiV1 reqDto) {

    ResStoreTimeSlotPutDTOApiV1 resDto = storeTimeSlotServiceApiV1.putById(
        timeSlotId,
        reqDto,
        storeManagerId,
        storeId,
        role);

    return new ResponseEntity<>(
        ResDTO.success(resDto),
        HttpStatus.OK
    );
  }

  //팝업스토어 시간대 조회
  @GetMapping("/timeslots/{timeSlotId}")
  public ResponseEntity<ResDTO<ResStoreTimeSlotGetDTOApiV1>> getById(
      @PathVariable UUID timeSlotId) {

    ResStoreTimeSlotGetDTOApiV1 resDto = storeTimeSlotServiceApiV1.getById(timeSlotId);

    return new ResponseEntity<>(
        ResDTO.success(resDto),
        HttpStatus.OK
    );
  }

  //팝업스토어 시간대 전체 조회
  @GetMapping("/{storeId}/timeslots")
  public ResponseEntity<ResDTO<ResStoreTimeSlotGetDTOApiV1>> getBy(
      @PathVariable UUID storeId,
      Pageable pageable) {

    ResStoreTimeSlotGetDTOApiV1 resDto = storeTimeSlotServiceApiV1.getBy(storeId,pageable);

    return new ResponseEntity<>(
          ResDTO.success(resDto),
          HttpStatus.OK
      );
    }
}
