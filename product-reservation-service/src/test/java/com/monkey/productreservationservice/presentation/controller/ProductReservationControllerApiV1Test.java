package com.monkey.productreservationservice.presentation.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.SimpleType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monkey.productreservationservice.application.dto.request.ReqProductReservationPostDTOApiV1;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
@ActiveProfiles("test")
public class ProductReservationControllerApiV1Test {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // 예약 생성
    @Test
    public void testProductReservationPostSuccess() throws Exception {
        ReqProductReservationPostDTOApiV1 reqDto = ReqProductReservationPostDTOApiV1.builder()
                .productReservation(
                        ReqProductReservationPostDTOApiV1.ProductReservation.builder()
                                .quantity(1)
                                .build()
                )
                .build();
        String reqDtoJson = objectMapper.writeValueAsString(reqDto);
        mockMvc.perform(
                RestDocumentationRequestBuilders.post("/v1/product-reservations/{productId}", UUID.randomUUID())
                        // .header(HttpHeaders.AUTHORIZATION, "Bearer " + resDto.getData().getAccessJwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reqDtoJson)
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.jsonPath("code").value("000")
                )
                .andDo(
                        document("상품 예약 성공",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(ResourceSnippetParameters.builder()
                                        .tag("PRODUCT Reservations v1")
                                        .summary("상품 예약")
                                        .description("""
                                                ## 상품 예약 엔드포인트 입니다.
                                                """)
                                        .pathParameters(
                                                parameterWithName("productId").type(SimpleType.STRING).description("상품 ID")
                                        )
                                        .requestFields(
                                                fieldWithPath("productReservation.quantity").type(JsonFieldType.NUMBER).description("구매 수량")
                                        )
                                        .responseFields(
                                                fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                                fieldWithPath("data.productReservation.productReservationId").type(JsonFieldType.STRING).optional().description("상품 예약 ID"),
                                                fieldWithPath("data.productReservation.productId").type(JsonFieldType.STRING).description("상품 ID"),
                                                fieldWithPath("data.productReservation.quantity").type(JsonFieldType.NUMBER).description("상품 수량")
                                        )
                                        .build()
                                )
                        )
                );

    }

    // 예약 취소
    @Test
    public void testProductReservationCancelPostSuccess() throws Exception {
        mockMvc.perform(
                RestDocumentationRequestBuilders.post("/v1/product-reservations/{productReservationId}/cancel", UUID.randomUUID())
                        // .header(HttpHeaders.AUTHORIZATION, "Bearer " + resDto.getData().getAccessJwt())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.jsonPath("code").value("000")
                )
                .andDo(
                        document("상품 예약 취소 성공",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(ResourceSnippetParameters.builder()
                                        .tag("PRODUCT Reservations v1")
                                        .summary("상품 예약 취소")
                                        .description("""
                                                ## 상품 예약 취소 엔드포인트 입니다.
                                                """)
                                        .pathParameters(
                                                parameterWithName("productReservationId").type(SimpleType.STRING).description("상품 예약 ID")
                                        )
                                        .responseFields(
                                                fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                                fieldWithPath("data.productReservation.productReservationId").type(JsonFieldType.STRING).description("상품 예약 ID"),
                                                fieldWithPath("data.productReservation.status").type(JsonFieldType.STRING).description("예약 상태")
                                        )
                                        .build()
                                )
                        )
                );
    }

    // 예약 상세 조회
    @Test
    public void testProductReservationGetByIdSuccess() throws Exception {
        mockMvc.perform(
                RestDocumentationRequestBuilders.get("/v1/product-reservations/{productReservationId}", UUID.randomUUID())
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.jsonPath("code").value("000")
                )
                .andDo(
                        document("상품 예약 상세 조회 성공",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(ResourceSnippetParameters.builder()
                                        .tag("PRODUCT Reservations v1")
                                        .summary("상품 예약 상세 조회")
                                        .description("""
                                                ## 상품 예약 상세 조회 엔드포인트 입니다.
                                                """)
                                        .pathParameters(
                                                parameterWithName("productReservationId").type(SimpleType.STRING).description("상품 예약 ID")
                                        )
                                        .responseFields(
                                                fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                                fieldWithPath("data.productReservation.productReservationId").type(JsonFieldType.STRING).description("상품 예약 ID"),
                                                fieldWithPath("data.productReservation.quantity").type(JsonFieldType.NUMBER).description("예약 수량"),
                                                fieldWithPath("data.productReservation.status").type(JsonFieldType.STRING).description("예약 상태"),
                                                fieldWithPath("data.productReservation.createdAt").type(JsonFieldType.STRING).optional().description("예약 생성일시"),

                                                fieldWithPath("data.productReservation.product.productId").type(JsonFieldType.STRING).description("상품 ID"),
                                                fieldWithPath("data.productReservation.product.productName").type(JsonFieldType.STRING).description("상품명"),

                                                fieldWithPath("data.productReservation.user.userId").type(JsonFieldType.NUMBER).description("유저 ID"),
                                                fieldWithPath("data.productReservation.user.userName").type(JsonFieldType.STRING).description("유저 이름"),

                                                fieldWithPath("data.productReservation.store.storeId").type(JsonFieldType.STRING).description("스토어 ID"),
                                                fieldWithPath("data.productReservation.store.storeName").type(JsonFieldType.STRING).description("스토어 이름")
                                        )
                                        .build()
                                )
                        )
                );
    }
}

