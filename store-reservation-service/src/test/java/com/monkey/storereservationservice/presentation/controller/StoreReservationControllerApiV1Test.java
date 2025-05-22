package com.monkey.storereservationservice.presentation.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monkey.storereservationservice.application.dto.request.ReqStoreReservationPostDTOApiV1;
import com.monkey.storereservationservice.config.StoreFeignClientMockConfig;
import com.monkey.storereservationservice.domain.entity.StoreReservationEntity;
import com.monkey.storereservationservice.domain.vo.StoreReservationStatus;
import com.monkey.storereservationservice.infrastructure.persistence.StoreReservationJpaRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.monkey.storereservationservice.config.StoreFeignClientMockConfig.FIXED_STORE_ID;
import static com.monkey.storereservationservice.config.StoreFeignClientMockConfig.FIXED_TIMESLOT_ID;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;

@Disabled
@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@Import(StoreFeignClientMockConfig.class)
public class StoreReservationControllerApiV1Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StoreReservationJpaRepository storeReservationJpaRepository;

    private UUID savedId;

    @BeforeEach
    void setUp() {
        savedId = storeReservationJpaRepository.save(
                StoreReservationEntity.createStoreReservation(
                        FIXED_TIMESLOT_ID,
                        1L,
                        1,
                        StoreReservationStatus.SCHEDULED
                )
        ).getStoreReservationId();

        storeReservationJpaRepository.save(
                StoreReservationEntity.createStoreReservation(
                        UUID.randomUUID(),
                        2L,
                        2,
                        StoreReservationStatus.CANCELED
                )
        );

        storeReservationJpaRepository.save(
                StoreReservationEntity.createStoreReservation(
                        UUID.randomUUID(),
                        3L,
                        3,
                        StoreReservationStatus.VISITED
                )
        );

        storeReservationJpaRepository.save(
                StoreReservationEntity.createStoreReservation(
                        UUID.randomUUID(),
                        4L,
                        4,
                        StoreReservationStatus.NO_SHOW
                )
        );
    }

    // 예약 생성
    @Test
    public void testStoreReservationPostSuccess() throws Exception {

        ReqStoreReservationPostDTOApiV1 reqDto = ReqStoreReservationPostDTOApiV1.builder()
                .storeReservation(
                        ReqStoreReservationPostDTOApiV1.StoreReservation.builder()
                                .timeSlotId(FIXED_TIMESLOT_ID)
                                .personCount(1)
                                .build()
                )
                .build();

        String reqDtoJson = objectMapper.writeValueAsString(reqDto);

        mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/v1/store-reservations")
                                .content(reqDtoJson)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.jsonPath("code").value("000")
                )
                .andDo(
                        document(
                                "STORE-RESERVATION 예약 생성 성공",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(ResourceSnippetParameters.builder()
                                        .tag("STORE-RESERVATION v1")
                                        .summary("팝업스토어 예약 생성")
                                        .description("""
                                                ## 팝업스토어 예약 생성 엔드포인트입니다.
                                                
                                                시간대 ID와 예약 인원을 입력하여 예약을 생성합니다.
                                                """)
                                        .requestFields(
                                                fieldWithPath("storeReservation.timeSlotId").type(JsonFieldType.STRING).description("예약 시간대 ID"),
                                                fieldWithPath("storeReservation.personCount").type(JsonFieldType.NUMBER).description("예약 인원 수")
                                        )
                                        .responseFields(
                                                fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                                fieldWithPath("data.storeReservation.storeReservationId").type(JsonFieldType.STRING).description("예약 ID"),
                                                fieldWithPath("data.storeReservation.status").type(JsonFieldType.STRING).description("예약 상태")
                                        )
                                        .build()
                                )
                        )
                );
    }

    // 예약 단건 상세 조회
    @Test
    public void testStoreReservationGetDetailSuccess() throws Exception {
        mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/v1/store-reservations/{reservationId}", savedId)
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.jsonPath("code").value("000")
                )
                .andDo(
                        document(
                                "STORE-RESERVATION 예약 상세조회 성공",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(ResourceSnippetParameters.builder()
                                        .tag("STORE-RESERVATION v1")
                                        .summary("팝업스토어 예약 상세 조회")
                                        .description("예약 ID로 예약 상세정보를 조회합니다.")
                                        .responseFields(
                                                fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                                fieldWithPath("data.storeReservation.storeReservationId").type(JsonFieldType.STRING).description("예약 ID"),
                                                fieldWithPath("data.storeReservation.status").type(JsonFieldType.STRING).description("예약 상태"),
                                                fieldWithPath("data.storeReservation.timeSlot.store.storeId").type(JsonFieldType.STRING).description("스토어 ID"),
                                                fieldWithPath("data.storeReservation.timeSlot.date").type(JsonFieldType.STRING).description("날짜"),
                                                fieldWithPath("data.storeReservation.timeSlot.entryTime").type(JsonFieldType.STRING).description("입장 시간"),
                                                fieldWithPath("data.storeReservation.timeSlot.exitTime").type(JsonFieldType.STRING).description("퇴장 시간"),
                                                fieldWithPath("data.storeReservation.user.userId").type(JsonFieldType.NUMBER).description("유저 ID"),
                                                fieldWithPath("data.storeReservation.user.userName").type(JsonFieldType.STRING).description("유저 이름")
                                        )
                                        .build()
                                )
                        )
                );
    }

    // 예약 전체 목록 조회
    @Test
    public void testStoreReservationSearchSuccess() throws Exception {
        mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/v1/store-reservations")
                                .param("userId", "1")
                                .param("storeId", FIXED_STORE_ID.toString())
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.jsonPath("code").value("000")
                )
                .andDo(
                        document(
                                "STORE-RESERVATION 예약 목록 조회 성공",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(ResourceSnippetParameters.builder()
                                        .tag("STORE-RESERVATION v1")
                                        .summary("팝업스토어 예약 목록 조회")
                                        .description("userId, storeId로 예약 목록을 조회할 수 있습니다.")
                                        .queryParameters(
                                                parameterWithName("userId").optional().description("유저 ID"),
                                                parameterWithName("storeId").optional().description("스토어 ID")
                                        )
                                        .responseFields(
                                                fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                                fieldWithPath("data.storeReservationList[]").type(JsonFieldType.ARRAY).description("예약 목록"),
                                                fieldWithPath("data.storeReservationList[].storeReservationId").type(JsonFieldType.STRING).description("예약 ID"),
                                                fieldWithPath("data.storeReservationList[].status").type(JsonFieldType.STRING).description("예약 상태"),
                                                fieldWithPath("data.storeReservationList[].timeSlot.store.storeId").type(JsonFieldType.STRING).description("스토어 ID"),
                                                fieldWithPath("data.storeReservationList[].timeSlot.date").type(JsonFieldType.STRING).description("날짜"),
                                                fieldWithPath("data.storeReservationList[].timeSlot.entryTime").type(JsonFieldType.STRING).description("입장 시간"),
                                                fieldWithPath("data.storeReservationList[].timeSlot.exitTime").type(JsonFieldType.STRING).description("퇴장 시간"),
                                                fieldWithPath("data.storeReservationList[].user.userId").type(JsonFieldType.NUMBER).description("유저 ID"),
                                                fieldWithPath("data.storeReservationList[].user.userName").type(JsonFieldType.STRING).description("유저 이름")
                                        )
                                        .build()
                                )
                        )
                );
    }

    @Test
    public void testStoreReservationStatusUpdateSuccess() throws Exception {
        // given
        StoreReservationStatus newStatus = StoreReservationStatus.VISITED;
        String requestJson = """
        {
            "storeReservation": {
                "status": "%s"
            }
        }
        """.formatted(newStatus.name());

        mockMvc.perform(
                        RestDocumentationRequestBuilders.put("/v1/store-reservations/{reservationId}/status", savedId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.jsonPath("code").value("000")
                )
                .andDo(
                        document(
                                "STORE-RESERVATION 예약 상태 변경 성공",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(ResourceSnippetParameters.builder()
                                        .tag("STORE-RESERVATION v1")
                                        .summary("팝업스토어 예약 상태 변경")
                                        .description("storeReservationID로 예약 상태를 변경합니다.")
                                        .requestFields(
                                                fieldWithPath("storeReservation.status").type(JsonFieldType.STRING).description("변경할 예약 상태 (예: SCHEDULED, CANCELED, VISITED, NO_SHOW)")
                                        )
                                        .responseFields(
                                                fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                                fieldWithPath("data.storeReservation.storeReservationId").type(JsonFieldType.STRING).description("예약 ID"),
                                                fieldWithPath("data.storeReservation.status").type(JsonFieldType.STRING).description("변경된 예약 상태")
                                        )
                                        .build()
                                )
                        )
                );
    }
}