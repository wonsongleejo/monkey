package com.monkey.storeservice.infrastructure.config;

import com.monkey.storeservice.domain.article.entity.StoreEntity;
import com.monkey.storeservice.domain.article.repository.StoreRepository;
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
            .storeId(UUID.randomUUID())
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
