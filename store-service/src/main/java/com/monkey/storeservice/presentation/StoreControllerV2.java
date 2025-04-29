package com.monkey.storeservice.presentation;

import com.monkey.storeservice.application.dto.response.ResStoreGetByIdDTOApiV1;
import com.monkey.storeservice.application.dto.response.ResStoreGetDTOApiV2;
import com.monkey.storeservice.application.service.v2.StoreServiceApiV2;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/stores")
public class StoreControllerV2 {

  private final StoreServiceApiV2 storeServiceApiV2;

  // 스토어 단건 조회
  @GetMapping("/{storeId}")
  public ResStoreGetByIdDTOApiV1 getById(
      @PathVariable UUID storeId
  ) {
    return storeServiceApiV2.getById(storeId);
  }

  // 스토어 리스트 조회 (페이징)
  @GetMapping
  public ResStoreGetDTOApiV2 getAll(
      Pageable pageable
  ) {
    return storeServiceApiV2.getBy(pageable);
  }
}
