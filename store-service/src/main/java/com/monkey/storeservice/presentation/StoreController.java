package com.monkey.storeservice.presentation;

import com.monkey.common_module.dto.ResDTO;
import com.monkey.storeservice.application.dto.request.ReqStorePostDTOApiV1;
import com.monkey.storeservice.application.dto.request.ReqStorePutDTOApiV1;
import com.monkey.storeservice.application.dto.response.ResStoreGetDTOApiV1;
import com.monkey.storeservice.application.dto.response.ResStorePutDTOApiV1;
import com.monkey.storeservice.application.dto.response.ResStorePostDTOApiV1;
import com.monkey.storeservice.application.service.StoreServiceApiV1;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/stores")
public class StoreController {

  private final StoreServiceApiV1 storeServiceApiV1;

  //  팝업스토어 생성
  @PostMapping
  public ResponseEntity<ResDTO<ResStorePostDTOApiV1>> postBy(@RequestBody @Valid ReqStorePostDTOApiV1 reqDto) {

    ResStorePostDTOApiV1 resDto = storeServiceApiV1.postBy(reqDto);

    return new ResponseEntity<>(
        ResDTO.success(resDto),
        HttpStatus.OK
    );

  }

  // 팝업스토어 수정
  @PutMapping("/{storeId}")
  public ResponseEntity<ResDTO<ResStorePutDTOApiV1>> putById(
      @PathVariable UUID storeId,
      @RequestBody @Valid ReqStorePutDTOApiV1 reqDto) {

    //Put요청 Dto에 있는 update메서드로 db업데이트
    //reqStorePutDtoApiV1.getStore().update(storeEntity);

    ResStorePutDTOApiV1 resDto = storeServiceApiV1.putById(storeId,reqDto);

    return new ResponseEntity<>(
        ResDTO.success(resDto),
        HttpStatus.OK
    );
  }

  // 팝업스토어 조회
  @GetMapping("/{storeId}")
  public ResponseEntity<ResDTO<ResStoreGetDTOApiV1>> getById(@PathVariable UUID storeId) {

    ResStoreGetDTOApiV1 resDto = storeServiceApiV1.getById(storeId);

    return new ResponseEntity<>(
        ResDTO.success(resDto),
        HttpStatus.OK
    );
  }

  // 팝업스토어 전체 조회
  @GetMapping
  public ResponseEntity<ResDTO<ResStoreGetDTOApiV1>>getBy(Pageable pageable) {

    ResStoreGetDTOApiV1 resDto = storeServiceApiV1.getBy(pageable);

    return new ResponseEntity<>(
        ResDTO.success(resDto),
        HttpStatus.OK
    );
  }
}

