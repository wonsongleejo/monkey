package com.monkey.productservice.presentation.controller;

import static org.mockito.BDDMockito.given;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.SimpleType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monkey.common_module.dto.ResDTO;
import com.monkey.productservice.application.dto.request.ReqProductPostDTOApiV1;
import com.monkey.productservice.application.dto.request.ReqProductPutDTOApiV1;
import com.monkey.productservice.domain.entity.ProductEntity;
import com.monkey.productservice.infrastructure.feignclient.StoreFeignClientApiV1;
import com.monkey.productservice.infrastructure.feignclient.dto.response.ResStoreClientGetByIdDTOApiV1;
import com.monkey.productservice.infrastructure.persistence.ProductJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @MockBean
    private StoreFeignClientApiV1 storeClient;

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
                                .purchaseLimitPerUser(3)
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
                                                
                                                ---
                                                
                                                """)
                                        .requestFields(
                                                fieldWithPath("product.storeId").type(JsonFieldType.STRING).description("스토어 ID"),
                                                fieldWithPath("product.productName").type(JsonFieldType.STRING).description("상품명"),
                                                fieldWithPath("product.price").type(JsonFieldType.NUMBER).description("상품 가격"),
                                                fieldWithPath("product.quantity").type(JsonFieldType.NUMBER).description("상품 수량"),
                                                fieldWithPath("product.purchaseLimitPerUser").type(JsonFieldType.NUMBER).description("상품 1인당 구매 수량 제한")
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
        // 실제 저장된 상품
        ProductEntity saved = productJpaRepository.save(
                ProductEntity.builder()
                        .storeId(UUID.randomUUID())
                        .productName("테스트 상품 ver1")
                        .price(10000)
                        .quantity(100)
                        .purchaseLimitPerUser(1)
                        .build()
        );

        ReqProductPutDTOApiV1 reqDto = ReqProductPutDTOApiV1.builder()
                .product(
                        ReqProductPutDTOApiV1.Product.builder()
                                .productName("테스트 상품 ver2")
                                .price(50000)
                                .quantity(200)
                                .purchaseLimitPerUser(2)
                                .build()
                )
                .build();
        String reqDtoJson = objectMapper.writeValueAsString(reqDto);
        mockMvc.perform(
                RestDocumentationRequestBuilders.put("/v1/products/{productId}", saved.getProductId())
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
                                                
                                                ---
                                                
                                                """)
                                        .pathParameters(
                                                parameterWithName("productId").type(SimpleType.STRING).description("상품 ID")
                                        )
                                        .requestFields(
                                                fieldWithPath("product.productName").type(JsonFieldType.STRING).description("상품명"),
                                                fieldWithPath("product.price").type(JsonFieldType.NUMBER).description("상품 가격"),
                                                fieldWithPath("product.quantity").type(JsonFieldType.NUMBER).description("상품 수량"),
                                                fieldWithPath("product.purchaseLimitPerUser").type(JsonFieldType.NUMBER).description("상품 1인당 구매 수량 제한")
                                        )
                                        .responseFields(
                                                fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                                fieldWithPath("data.product.productName").type(JsonFieldType.STRING).description("상품명"),
                                                fieldWithPath("data.product.price").type(JsonFieldType.NUMBER).description("상품 가격"),
                                                fieldWithPath("data.product.quantity").type(JsonFieldType.NUMBER).description("상품 수량"),
                                                fieldWithPath("data.product.purchaseLimitPerUser").type(JsonFieldType.NUMBER).description("상품 1인당 구매 수량 제한")
                                        )
                                        .build()
                                )
                        )
                );
    }

    // 상품 단건 조회
    @Test
    public void testProductGetByIdSuccess() throws Exception {
        ProductEntity saved = productJpaRepository.findAllByIsDeletedFalse().get(0);

        given(storeClient.getStoreById(saved.getStoreId()))
                .willReturn(
                        ResDTO.<ResStoreClientGetByIdDTOApiV1>builder()
                                .code("000")
                                .message("성공")
                                .data(
                                        ResStoreClientGetByIdDTOApiV1.builder()
                                                .storeId(saved.getStoreId())
                                                .storeName("테스트 스토어")
                                                .build()
                                )
                                .build()
        );

        mockMvc.perform(
                RestDocumentationRequestBuilders.get("/v1/products/{productId}", saved.getProductId())
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.jsonPath("code").value("000")
                )
                .andDo(
                        document("상품 단건 조회 성공",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(ResourceSnippetParameters.builder()
                                        .tag("PRODUCTS v1")
                                        .summary("상품 단건 조회")
                                        .description("""
                                                ## 상품 단건 조회 엔드포인트 입니다.
                                                
                                                ---
                                                
                                                """)
                                        .pathParameters(
                                                parameterWithName("productId").type(SimpleType.STRING).description("상품 ID")
                                        )
                                        .responseFields(
                                                fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                                fieldWithPath("data.product.productId").type(JsonFieldType.STRING).description("상품 ID"),
                                                fieldWithPath("data.product.productName").type(JsonFieldType.STRING).description("상품명"),
                                                fieldWithPath("data.product.price").type(JsonFieldType.NUMBER).description("상품 가격"),
                                                fieldWithPath("data.product.quantity").type(JsonFieldType.NUMBER).description("상품 수량"),
                                                fieldWithPath("data.product.purchaseLimitPerUser").type(JsonFieldType.NUMBER).description("1인당 구매 수량 제한"),
                                                fieldWithPath("data.product.createdBy").type(JsonFieldType.STRING).optional().description("상품등록자"),
                                                fieldWithPath("data.product.store.storeId").type(JsonFieldType.STRING).description("스토어 ID"),
                                                fieldWithPath("data.product.store.storeName").type(JsonFieldType.STRING).description("스토어 이름")
                                        )
                                        .build()
                                )
                        )
                );
    }

    // 상품 전체 조회
    @Test
    public void testProductGetSuccess() throws Exception {
        List<ProductEntity> savedList = productJpaRepository.findAllByIsDeletedFalse();

        mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/v1/products")
                                .param("page", "0")
                                .param("size", "10")
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.jsonPath("code").value("000"),
                        MockMvcResultMatchers.jsonPath("data.productList").isArray(),
                        MockMvcResultMatchers.jsonPath("data.productList.length()").value(savedList.size())
                )
                .andDo(
                        document("상품 전체 조회 성공",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(ResourceSnippetParameters.builder()
                                        .tag("PRODUCTS v1")
                                        .summary("상품 전체 조회")
                                        .description("""
                                            ## 상품 전체 조회 엔드포인트입니다.
                                            
                                            ---
                                            
                                            """)
                                        .queryParameters(
                                                parameterWithName("page").description("페이지 번호 (0부터 시작)").optional(),
                                                parameterWithName("size").description("페이지당 아이템 수").optional()
                                        )
                                        .responseFields(
                                                fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                                fieldWithPath("data.productList[].productId").type(JsonFieldType.STRING).description("상품 ID"),
                                                fieldWithPath("data.productList[].storeId").type(JsonFieldType.STRING).description("스토어 ID"),
                                                fieldWithPath("data.productList[].productName").type(JsonFieldType.STRING).description("상품명"),
                                                fieldWithPath("data.productList[].price").type(JsonFieldType.NUMBER).description("가격"),
                                                fieldWithPath("data.productList[].quantity").type(JsonFieldType.NUMBER).description("수량"),
                                                fieldWithPath("data.productList[].purchaseLimitPerUser").type(JsonFieldType.NUMBER).description("1인당 구매 제한")
                                        )
                                        .build()
                                )
                        )
                );
    }

}
