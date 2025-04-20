package com.monkey.storeservice.infrastructure.config;

import com.monkey.storeservice.domain.entity.StoreEntity;
import com.monkey.storeservice.domain.entity.StoreEntity.OpenStatus;
import com.monkey.storeservice.domain.repository.StoreRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StoreDataLoader {

  @Bean
  public CommandLineRunner loadData(StoreRepository storeRepository) {
    return args -> {
      if(storeRepository.count()==0){
        StoreEntity storeEntity = StoreEntity.builder()
            .storeId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
            .openStatus(OpenStatus.CLOSED)
            .storeName("팝업스토어")
            .description("팝업스토어 입니다")
            .startDate(LocalDate.parse("2025-04-17"))
            .endDate(LocalDate.parse("2025-04-21"))
            .startTime(LocalTime.parse("10:00"))
            .endTime(LocalTime.parse("18:00"))
            .totalPersonCount(100)
            .build();
        storeRepository.save(storeEntity);
      }
    };
  }
}
