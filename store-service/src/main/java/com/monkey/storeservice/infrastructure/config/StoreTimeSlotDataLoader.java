package com.monkey.storeservice.infrastructure.config;

import com.monkey.storeservice.domain.article.entity.StoreTimeSlotEntity;
import com.monkey.storeservice.domain.article.repository.StoreTimeSlotRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StoreTimeSlotDataLoader {

  @Bean
  public CommandLineRunner loadStoreTimeSlotData(StoreTimeSlotRepository storeTimeSlotRepository) {
    return args -> {
      if (storeTimeSlotRepository.count() == 0) {
        StoreTimeSlotEntity storeTimeSlotEntity = StoreTimeSlotEntity.builder()
            .storeId(UUID.randomUUID())
            .timeSlotId(UUID.randomUUID())
            .slotDate(LocalDate.parse("2025-04-17"))
            .entryTime(LocalTime.parse("10:00"))
            .exitTime(LocalTime.parse("12:00"))
            .maxPerson(20)
            .build();
        storeTimeSlotRepository.save(storeTimeSlotEntity);
      }
    };
  }
}
