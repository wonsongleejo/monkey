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
@Profile("dev")
public class StoreReservationDataLoader {

    @Bean
    public CommandLineRunner loadData(StoreReservationRepository storeReservationRepository) {
        return args -> {
            if (storeReservationRepository.count() == 0) {
                StoreReservationEntity storeReservationEntity = StoreReservationEntity.createStoreReservation(
                        UUID.randomUUID(),
                        1L,
                        2,
                        StoreReservationStatus.SCHEDULED
                );
                storeReservationRepository.save(storeReservationEntity);
            }
        };
    }
}