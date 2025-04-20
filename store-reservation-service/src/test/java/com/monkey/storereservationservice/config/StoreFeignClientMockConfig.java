package com.monkey.storereservationservice.config;

import com.monkey.storereservationservice.infrastructure.client.StoreClient;
import com.monkey.storereservationservice.infrastructure.dto.response.ResStoreTimeSlotDTOApiV1;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@TestConfiguration
public class StoreFeignClientMockConfig {

    // 테스트용으로 DB에 저장한 값
    public static final UUID FIXED_TIMESLOT_ID = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    public static final UUID FIXED_STORE_ID = UUID.fromString("11111111-aaaa-bbbb-cccc-111111111111");

    @Bean
    @Primary
    public StoreClient storeClient() {
        StoreClient mock = Mockito.mock(StoreClient.class);

        given(mock.getTimeSlotById(any())).willAnswer(invocation -> {
            return ResStoreTimeSlotDTOApiV1.builder()
                    .timeSlotId((UUID) invocation.getArgument(0))
                    .date(LocalDate.of(2025, 4, 20))
                    .entryTime(LocalTime.of(10, 0))
                    .exitTime(LocalTime.of(11, 0))
                    .store(ResStoreTimeSlotDTOApiV1.StoreInfo.builder()
                            .storeId(FIXED_STORE_ID)
                            .storeName("테스트스토어")
                            .build())
                    .build();
        });

        return mock;
    }
}