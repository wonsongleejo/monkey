package com.monkey.storeservice.application.service.v2;

import com.monkey.common_module.dto.ResponseCode;
import com.monkey.common_module.exception.CustomException;
import com.monkey.storeservice.application.dto.response.ResStoreGetByIdDTOApiV1;
import com.monkey.storeservice.application.dto.response.ResStoreGetDTOApiV2;
import com.monkey.storeservice.domain.entity.StoreEntity;
import com.monkey.storeservice.domain.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreServiceImplApiV2 implements StoreServiceApiV2 {

  private final StoreRepository storeRepository;
  private final RedisTemplate<String, Object> redisTemplate; // RedisTemplate 하나만 사용

  private static final String STORE_KEY_PREFIX = "store:";
  private static final long CACHE_TTL_MINUTES = 30;

  @Override
  public ResStoreGetByIdDTOApiV1 getById(UUID storeId) {
    String key = generateCacheKey(storeId);

    // 1. Redis 조회
    Object cached = redisTemplate.opsForValue().get(key);
    if (cached instanceof ResStoreGetByIdDTOApiV1 resDto) {
      return resDto;
    }

    // 2. DB 조회
    StoreEntity storeEntity = storeRepository.findById(storeId)
        .orElseThrow(() -> new CustomException(ResponseCode.STORE_NOT_FOUND));

    ResStoreGetByIdDTOApiV1 resDto = ResStoreGetByIdDTOApiV1.of(storeEntity);

    // 3. Redis 저장
    redisTemplate.opsForValue().set(key, resDto, CACHE_TTL_MINUTES, TimeUnit.MINUTES);

    return resDto;
  }

  @Override
  public ResStoreGetDTOApiV2 getBy(Pageable pageable) {
    String key = generateListCacheKey(pageable);

    // 1. Redis 조회
    Object cached = redisTemplate.opsForValue().get(key);
    if (cached instanceof ResStoreGetDTOApiV2 resDto) {
      return resDto;
    }

    // 2. DB 조회
    Page<StoreEntity> storePage = storeRepository.findAll(pageable);
    ResStoreGetDTOApiV2 result = ResStoreGetDTOApiV2.of(storePage);

    // 3. Redis 저장
    redisTemplate.opsForValue().set(key, result, CACHE_TTL_MINUTES, TimeUnit.MINUTES);

    return result;
  }

  // 스토어 단건 조회용 키
  private String generateCacheKey(UUID storeId) {
    return STORE_KEY_PREFIX + storeId;
  }

  // 스토어 리스트 조회용 키
  private String generateListCacheKey(Pageable pageable) {
    return STORE_KEY_PREFIX + "list:page=" + pageable.getPageNumber() + "_size=" + pageable.getPageSize();
  }
}