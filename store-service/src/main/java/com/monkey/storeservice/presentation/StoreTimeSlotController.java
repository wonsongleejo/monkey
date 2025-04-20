package com.monkey.storeservice.presentation;

import com.monkey.common_module.dto.ResDTO;
import com.monkey.storeservice.application.dto.request.ReqStoreTimeSlotPostDTOApiV1;
import com.monkey.storeservice.application.dto.request.ReqStoreTimeSlotPutDTOApiV1;
import com.monkey.storeservice.application.dto.response.ResStoreTimeSlotGetDTOApiV1;
import com.monkey.storeservice.application.dto.response.ResStoreTimeSlotPostDTOApiV1;
import com.monkey.storeservice.application.dto.response.ResStoreTimeSlotPutDTOApiV1;
import com.monkey.storeservice.application.service.StoreTimeSlotServiceApiV1;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/timeslots")
@RequiredArgsConstructor
public class StoreTimeSlotController {

  private final StoreTimeSlotServiceApiV1 storeTimeSlotServiceApiV1;

  //  팝업스토어 시간대 생성
  @PostMapping("/{storeId}")
  public ResponseEntity<ResDTO<ResStoreTimeSlotPostDTOApiV1>> postById(
      @PathVariable UUID storeId,
      @RequestBody ReqStoreTimeSlotPostDTOApiV1 reqDto) {

    ResStoreTimeSlotPostDTOApiV1 resDto = storeTimeSlotServiceApiV1.postById(storeId, reqDto);

    return new ResponseEntity<>(
        ResDTO.success(resDto),
        HttpStatus.OK
    );
  }

  //팝업스토어 시간대 수정
  @PutMapping("/{timeSlotId}")
  public ResponseEntity<ResDTO<ResStoreTimeSlotPutDTOApiV1>> putById(
      @PathVariable UUID timeSlotId,
      @RequestBody ReqStoreTimeSlotPutDTOApiV1 reqDto) {

    ResStoreTimeSlotPutDTOApiV1 resDto = storeTimeSlotServiceApiV1.putById(timeSlotId, reqDto);

    return new ResponseEntity<>(
        ResDTO.success(resDto),
        HttpStatus.OK
    );
  }

  //팝업스토어 시간대 조회
  @GetMapping("/{timeSlotId}")
  public ResponseEntity<ResDTO<ResStoreTimeSlotGetDTOApiV1>> getById(
      @PathVariable UUID timeSlotId) {

    ResStoreTimeSlotGetDTOApiV1 resDto = storeTimeSlotServiceApiV1.getById(timeSlotId);

    return new ResponseEntity<>(
        ResDTO.success(resDto),
        HttpStatus.OK
    );
  }

  //팝업스토어 시간대 전체 조회
  @GetMapping
  public ResponseEntity<ResDTO<ResStoreTimeSlotGetDTOApiV1>> getBy(Pageable pageable) {

    ResStoreTimeSlotGetDTOApiV1 resDto = storeTimeSlotServiceApiV1.getBy(pageable);

    return new ResponseEntity<>(
          ResDTO.success(resDto),
          HttpStatus.OK
      );
    }
}
