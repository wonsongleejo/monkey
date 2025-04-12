package com.monkey.productservice.presentation.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.SimpleType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monkey.productservice.application.dto.request.ReqProductPostDTOApiV1;
import com.monkey.productservice.application.dto.request.ReqProductPutDTOApiV1;
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
public class ProductControllerApiV1Test {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // 상품 등록
    @Test
    public void testProductPostSuccess() throws Exception {
        ReqProductPostDTOApiV1 reqDto = ReqProductPostDTOApiV1.builder()
                .product(
                        ReqProductPostDTOApiV1.Product.builder()
                                .storeId(UUID.randomUUID())
                                .productName("테스트 상품")
                                .price(10000)
                                .quantity(20)
                                .build()
                )
                 .build();

        String reqDtoJson = objectMapper.writeValueAsString(reqDto);

        mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/v1/products")
//                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + resDto.getData().getAccessJwt())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(reqDtoJson)
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.jsonPath("code").value("000")
                )
                .andDo(
                        document("상품 등록 성공",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(ResourceSnippetParameters.builder()
                                        .tag("PRODUCTS v1")
                                        .summary("상품 등록")
                                        .description("""
                                                ## 상품 등록 엔드포인트 입니다.
                                                """)
                                        .requestFields(
                                                fieldWithPath("product.storeId").type(JsonFieldType.STRING).description("스토어 ID"),
                                                fieldWithPath("product.productName").type(JsonFieldType.STRING).description("상품명"),
                                                fieldWithPath("product.price").type(JsonFieldType.NUMBER).description("상품 가격"),
                                                fieldWithPath("product.quantity").type(JsonFieldType.NUMBER).description("상품 수량")
                                        )
                                        .responseFields(
                                                fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                                fieldWithPath("data.product.productId").type(JsonFieldType.STRING).optional().description("상품 ID")
                                        )
                                        .build()
                                )
                        )
                );
    }

    // 상품 수정
    @Test
    public void testProductPutSuccess() throws Exception {
        ReqProductPutDTOApiV1 reqDto = ReqProductPutDTOApiV1.builder()
                .product(
                        ReqProductPutDTOApiV1.Product.builder()
                                .productName("테스트 상품 ver2")
                                .price(50000)
                                .quantity(200)
                                .build()
                )
                .build();
        String reqDtoJson = objectMapper.writeValueAsString(reqDto);
        mockMvc.perform(
                RestDocumentationRequestBuilders.put("/v1/products/{productId}", UUID.randomUUID())
//                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + resDto.getData().getAccessJwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reqDtoJson)
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.jsonPath("code").value("000")
                )
                .andDo(
                        document("상품 수정 성공",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(ResourceSnippetParameters.builder()
                                        .tag("PRODUCTS v1")
                                        .summary("상품 수정")
                                        .description("""
                                                ## 상품 수정 엔드포인트 입니다.
                                                """)
                                        .pathParameters(
                                                parameterWithName("productId").type(SimpleType.STRING).description("상품 ID")
                                        )
                                        .requestFields(
                                                fieldWithPath("product.productName").type(JsonFieldType.STRING).description("상품명"),
                                                fieldWithPath("product.price").type(JsonFieldType.NUMBER).description("상품 가격"),
                                                fieldWithPath("product.quantity").type(JsonFieldType.NUMBER).description("상품 수량")
                                        )
                                        .responseFields(
                                                fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                                fieldWithPath("data.product.productName").type(JsonFieldType.STRING).description("상품명"),
                                                fieldWithPath("data.product.price").type(JsonFieldType.NUMBER).description("상품 가격"),
                                                fieldWithPath("data.product.quantity").type(JsonFieldType.NUMBER).description("상품 수량")
                                        )
                                        .build()
                                )
                        )
                );
    }

}
