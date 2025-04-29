package com.monkey.storeservice.application.service.v2;

import com.monkey.storeservice.application.dto.response.ResStoreGetByIdDTOApiV1;
import com.monkey.storeservice.application.dto.response.ResStoreGetDTOApiV2;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface StoreServiceApiV2 {

  ResStoreGetByIdDTOApiV1 getById(UUID storeId);

  ResStoreGetDTOApiV2 getBy(Pageable pageable);

}