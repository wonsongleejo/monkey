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
    public void testSlackPostSuccess() throws Exception {
        ReqSlackStoreReservationPostDTOApiV1 reqDto = ReqSlackStoreReservationPostDTOApiV1.builder()
                .slack(
                        ReqSlackStoreReservationPostDTOApiV1.SlackMessage.builder()
                                .slackId(UUID.randomUUID())
                                .slackMessage("테스트 슬랙 메세지입니다.")
                                .build()
                )
                .build();

        String reqDtoJson = objectMapper.writeValueAsString(reqDto);

        mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/v1/slacks")
                                .content(reqDtoJson)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.jsonPath("code").value("000")
                )
                .andDo(
                        document(
                                "SLACK 메시지 생성 성공",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(
                                        ResourceSnippetParameters.builder()
                                                .tag("SLACK v1")
                                                .summary("SLACK 메시지 생성")
                                                .description("""
                                                        ## SLACK 메시지 생성 엔드포인트 입니다.
                                                        
                                                        ---
                                                        
                                                        예약 생성 시 Slack 메시지를 생성합니다.
                                                        """)
                                                .requestFields(
                                                        fieldWithPath("slack.slackId").type(JsonFieldType.STRING).description("Slack ID"),
                                                        fieldWithPath("slack.slackMessage").type(JsonFieldType.STRING).description("Slack 메시지")
                                                )
                                                .build()
                                )
                        )
                );
    }
}