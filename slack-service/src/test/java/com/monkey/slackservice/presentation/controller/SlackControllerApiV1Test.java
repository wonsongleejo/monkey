package com.monkey.slackservice.presentation.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monkey.slackservice.application.dto.request.ReqSlackStoreReservationPostDTOApiV1;
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
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class SlackControllerApiV1Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // 슬랙 메시지 생성
    @Test
    void slackMessageSendSuccessAndSaveToDb() throws Exception {
        String slackUserId = "D089A93Q285";

        ReqSlackStoreReservationPostDTOApiV1 reqDto = ReqSlackStoreReservationPostDTOApiV1.builder()
                .slack(
                        ReqSlackStoreReservationPostDTOApiV1.SlackMessage.builder()
                                .slackId(slackUserId)
                                .slackMessage("슬랙 메시지 연동 테스트입니다.")
                                .build()
                )
                .build();

        String reqBody = objectMapper.writeValueAsString(reqDto);

        mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/v1/slacks")
                                .content(reqBody)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("000"))
                .andDo(
                        document(
                                "SLACK 메시지 생성 성공",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(ResourceSnippetParameters.builder()
                                        .tag("SLACK v1")
                                        .summary("Slack 메시지 전송")
                                        .description("""
                                                예약 완료 또는 취소 시 Slack 메시지를 전송하는 API입니다.
                                                
                                                ---
                                                
                                                요청 시 Slack 메시지를 전송하고, DB에 로그를 저장합니다.
                                                """)
                                        .requestFields(
                                                fieldWithPath("slack.slackId").type(JsonFieldType.STRING).description("Slack 사용자 ID"),
                                                fieldWithPath("slack.slackMessage").type(JsonFieldType.STRING).description("보낼 메시지 내용")
                                        )
                                        .responseFields(
                                                fieldWithPath("code").description("응답 코드"),
                                                fieldWithPath("message").description("응답 메시지"),

                                                fieldWithPath("data.storeReservation.storeReservationId").description("예약 ID"),
                                                fieldWithPath("data.storeReservation.status").description("예약 상태"),

                                                fieldWithPath("data.storeReservation.timeSlot.store.storeId").description("팝업스토어 ID"),
                                                fieldWithPath("data.storeReservation.timeSlot.date").description("예약 날짜"),
                                                fieldWithPath("data.storeReservation.timeSlot.entryTime").description("입장 시간"),
                                                fieldWithPath("data.storeReservation.timeSlot.exitTime").description("퇴장 시간"),

                                                fieldWithPath("data.storeReservation.user.userId").description("유저 ID"),
                                                fieldWithPath("data.storeReservation.user.userName").description("유저 이름")
                                        )
                                        .build()
                                )
                        )
                );
    }
}