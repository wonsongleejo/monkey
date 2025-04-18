package com.monkey.storereservationservice.infrastructure.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.monkey.storereservationservice.StoreReservationServiceApplication;
import com.monkey.storereservationservice.infrastructure.dto.response.ResStoreTimeSlotDTOApiV1;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = StoreReservationServiceApplication.class
)
@ActiveProfiles("test")
class StoreClientTest {

    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(options().dynamicPort())
            .build();

    @DynamicPropertySource
    static void registerWireMockProperties(DynamicPropertyRegistry registry) {
        registry.add("store-service.url", wireMockServer::baseUrl);
    }

    @Autowired
    StoreClient storeClient;

    @Autowired
    ObjectMapper objectMapper;

    static final UUID TEST_TIME_SLOT_ID = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

    @Test
    void getTimeSlotById_success() throws Exception {
        // given
        ResStoreTimeSlotDTOApiV1 responseDto = ResStoreTimeSlotDTOApiV1.builder()
                .timeSlotId(TEST_TIME_SLOT_ID)
                .store(ResStoreTimeSlotDTOApiV1.StoreInfo.builder()
                        .storeId(UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"))
                        .storeName("테스트매장")
                        .build())
                .date(LocalDate.of(2025, 4, 20))
                .entryTime(LocalTime.of(10, 0))
                .exitTime(LocalTime.of(11, 0))
                .build();

        wireMockServer.stubFor(get(urlEqualTo("/v1/timeslots/" + TEST_TIME_SLOT_ID))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(responseDto)))
        );

        // when
        ResStoreTimeSlotDTOApiV1 result = storeClient.getTimeSlotById(TEST_TIME_SLOT_ID);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getStore().getStoreName()).isEqualTo("테스트매장");
    }
}