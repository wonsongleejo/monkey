package com.monkey.storereservationservice.presentation.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monkey.storereservationservice.application.dto.request.ReqStoreReservationPostDTOApiV1;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class StoreReservationControllerApiV1Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // 예약 생성
    @Test
    public void testStoreReservationPostSuccess() throws Exception {
        ReqStoreReservationPostDTOApiV1 reqDto = ReqStoreReservationPostDTOApiV1.builder()
                .storeReservation(
                        ReqStoreReservationPostDTOApiV1.StoreReservation.builder()
                                .timeSlotId(UUID.randomUUID())
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
                                        .build()
                                )
                        )
                );
    }

    // 예약 단건 상세 조회
    @Test
    public void testStoreReservationGetDetailSuccess() throws Exception {
        mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/v1/store-reservations/{reservationId}", UUID.randomUUID())
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
                                        .description("""
                                                ## 팝업스토어 예약 상세 조회 엔드포인트입니다.
                                                
                                                예약 ID로 예약 상세정보를 조회합니다.
                                                """)
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
                                .param("storeId", UUID.randomUUID().toString())
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
                                        .description("""
                                                ## 팝업스토어 예약 목록 조회 엔드포인트입니다.
                                                
                                                userId, storeId로 예약 목록을 조회할 수 있습니다.
                                                """)
                                        .build()
                                )
                        )
                );
    }

    // 예약 취소
    @Test
    public void testStoreReservationCancelSuccess() throws Exception {
        mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/v1/store-reservations/{reservationId}/cancel", UUID.randomUUID())
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.jsonPath("code").value("000")
                )
                .andDo(
                        document(
                                "STORE-RESERVATION 예약 취소 성공",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(ResourceSnippetParameters.builder()
                                        .tag("STORE-RESERVATION v1")
                                        .summary("팝업스토어 예약 취소")
                                        .description("""
                                                ## 팝업스토어 예약 취소 엔드포인트입니다.
                                                
                                                storeReservationID로 예약을 취소합니다.
                                                """)
                                        .build()
                                )
                        )
                );
    }
}