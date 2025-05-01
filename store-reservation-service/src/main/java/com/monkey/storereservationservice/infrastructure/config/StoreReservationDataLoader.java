package com.monkey.storereservationservice.infrastructure.config;

import com.monkey.storereservationservice.domain.entity.StoreReservationEntity;
import com.monkey.storereservationservice.domain.repository.StoreReservationRepository;
import com.monkey.storereservationservice.domain.vo.StoreReservationStatus;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.UUID;

@Configuration
@Profile("dev") // dev 프로필일 때만 Bean이 등록된다.
public class StoreReservationDataLoader {

    @Bean
    public CommandLineRunner loadData(StoreReservationRepository storeReservationRepository) {
        return args -> {
            if (storeReservationRepository.count() == 0) { // 데이터가 없을 경우에만 더미 데이터 추가
                StoreReservationEntity storeReservationEntity = StoreReservationEntity.builder()
                        .timeSlotId(UUID.randomUUID())
                        .userId(1L)
                        .personCount(2)
                        .status(StoreReservationStatus.SCHEDULED)
                        .build();
                storeReservationRepository.save(storeReservationEntity);
            }
        };
    }
}