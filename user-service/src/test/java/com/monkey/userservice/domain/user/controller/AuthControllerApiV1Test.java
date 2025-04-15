package com.monkey.userservice.domain.user.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monkey.userservice.application.dto.request.ReqAuthPostSignInDTOApiV1;
import com.monkey.userservice.application.dto.request.ReqAuthPostSingUpDTOApiV1;
import com.monkey.userservice.domain.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Transactional
//@ActiveProfiles("test") //Eureka 실행 없이 하기 위함
public class AuthControllerApiV1Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // 회원가입
    @Test
    public void testAuthPostSignUpSuccess() throws Exception {
        ReqAuthPostSingUpDTOApiV1 reqDto = ReqAuthPostSingUpDTOApiV1.builder()
                .user(
                        ReqAuthPostSingUpDTOApiV1.User.builder()
                                .username("testUser")
                                .password("test1234")
                                .slackId("slackID")
                                .role(UserEntity.Role.USER)
                                .build()
                )
                .build();

        String reqDtoJson = objectMapper.writeValueAsString(reqDto);

        mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/v1/auth/sign-up")
                                .content(reqDtoJson)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.jsonPath("code").value("000")
                )
                .andDo(
                        document("AUTH 회원가입 성공",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(ResourceSnippetParameters.builder()
                                        .tag("AUTH v1")
                                        .summary("AUTH 회원가입")
                                        .description("""
                                                ## AUTH 회원가입 엔드포인트 입니다.
                                                
                                                ---
                                                
                                                username, password, slackId를 입력받아 회원가입을 진행합니다.
                                                """)
                                        .requestFields(
                                                fieldWithPath("user.username").type(JsonFieldType.STRING).description("사용자 아이디"),
                                                fieldWithPath("user.password").type(JsonFieldType.STRING).description("사용자 비밀번호"),
                                                fieldWithPath("user.slackId").type(JsonFieldType.STRING).description("사용자 슬랙 아이디"),
                                                fieldWithPath("user.role").type(JsonFieldType.STRING).description("사용자 권한")
                                        )
                                        .build()
                                )
                        )
                )
                .andReturn();
    }

    // 로그인
    @Test
    public void testAuthPostSignInSuccess() throws Exception {
        ReqAuthPostSignInDTOApiV1 reqDto = ReqAuthPostSignInDTOApiV1.builder()
                .user(
                        ReqAuthPostSignInDTOApiV1.User.builder()
                                .username("testUset")
                                .password("test123")
                                .build()
                )
                .build();

        String reqDtoJson = objectMapper.writeValueAsString(reqDto);
        mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/v1/auth/sign-in")
                                .content(reqDtoJson)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.jsonPath("code").value("000")
                )
                .andDo(
                        document("AUTH 로그인 성공",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(ResourceSnippetParameters.builder()
                                        .tag("AUTH v1")
                                        .summary("AUTH 로그인")
                                        .description("""
                                                ## AUTH 로그인 엔드포인트 입니다.
                                                
                                                ---
                                                
                                                username, password 입력받아 로그인을 진행합니다.
                                                """)
                                        .requestFields(
                                                fieldWithPath("user.username").type(JsonFieldType.STRING).description("사용자 아이디"),
                                                fieldWithPath("user.password").type(JsonFieldType.STRING).description("사용자 비밀번호")
                                        )
                                        .build()
                                )
                        )
                );
    }

}