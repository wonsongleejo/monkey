package com.monkey.productreservationservice.presentation.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.SimpleType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monkey.common_module.dto.ResDTO;
import com.monkey.productreservationservice.ProductReservationServiceApplication;
import com.monkey.productreservationservice.application.dto.request.ReqProductReservationPostDTOApiV1;
import com.monkey.productreservationservice.config.TestFeignClientConfig;
import com.monkey.productreservationservice.domain.entity.ProductReservationEntity;
import com.monkey.productreservationservice.domain.vo.ProductReservationStatus;
import com.monkey.productreservationservice.infrastructure.feignclient.ProductFeignClientApiV1;
import com.monkey.productreservationservice.infrastructure.feignclient.StoreFeignClientApiV1;
import com.monkey.productreservationservice.infrastructure.feignclient.StoreReservationFeignClientApiV1;
import com.monkey.productreservationservice.infrastructure.feignclient.UserFeignClientApiV1;
import com.monkey.productreservationservice.infrastructure.feignclient.dto.response.ResProductClientGetByIdDTOApiV1;
import com.monkey.productreservationservice.infrastructure.feignclient.dto.response.ResStoreClientGetByIdDTOApiV1;
import com.monkey.productreservationservice.infrastructure.feignclient.dto.response.ResStoreReservationClientGetDTOApiV1;
import com.monkey.productreservationservice.infrastructure.feignclient.dto.response.ResUserClientGetByIdDTOApiV1;
import com.monkey.productreservationservice.infrastructure.persistence.ProductReservationJpaRepository;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

@SpringBootTest(classes = {ProductReservationServiceApplication.class, TestFeignClientConfig.class})
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
@Import(TestFeignClientConfig.class)
@ActiveProfiles("test")
public class ProductReservationControllerApiV1Test {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductReservationJpaRepository productReservationJpaRepository;

    @Autowired
    private ProductFeignClientApiV1 productClient;

    @Autowired
    private StoreFeignClientApiV1 storeClient;

    @Autowired
    private StoreReservationFeignClientApiV1 storeReservationClient;

    @Autowired
    private UserFeignClientApiV1 userClient;

    private UUID testProductId = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    private UUID testStoreId = UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb");
    private long testUserId = 123L;
    private String testSlackId = "cccccccc-cccc-cccc-cccc-cccccccccccc";

    @BeforeEach
    void setUpFeignClientMocks() {
        given(productClient.getProductById(testProductId))
                .willReturn(ResDTO.success(
                        ResProductClientGetByIdDTOApiV1.builder()
                                .product(
                                        ResProductClientGetByIdDTOApiV1.Product.builder()
                                                .productId(testProductId)
                                                .productName("카카오프렌즈 라이언 바디필로우")
                                                .quantity(100)
                                                .purchaseLimitPerUser(3)
                                                .store(
                                                        ResProductClientGetByIdDTOApiV1.Product.Store.builder()
                                                                .storeId(testStoreId)
                                                                .storeName("카카오프렌즈 팝업스토어 강남점")
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                ));

        given(storeReservationClient.getReservationsByStoreId(testStoreId, testUserId))
                .willReturn(ResDTO.success(
                        ResStoreReservationClientGetDTOApiV1.builder()
                                .storeReservationList(List.of(
                                        ResStoreReservationClientGetDTOApiV1.StoreReservation.builder()
                                                .user(
                                                        ResStoreReservationClientGetDTOApiV1.StoreReservation.User.builder()
                                                                .userId(testUserId)
                                                                .build()
                                                )
                                                .timeSlot(
                                                        ResStoreReservationClientGetDTOApiV1.StoreReservation.TimeSlot.builder()
                                                                .store(
                                                                        ResStoreReservationClientGetDTOApiV1.StoreReservation.TimeSlot.Store.builder()
                                                                                .storeId(testStoreId)
                                                                                .build()
                                                                )
                                                                .build()
                                                )
                                                .build()
                                ))
                                .build()
                ));

        given(storeClient.getStoreById(testStoreId))
                .willReturn(ResDTO.success(
                        ResStoreClientGetByIdDTOApiV1.builder()
                                .store(
                                        ResStoreClientGetByIdDTOApiV1.Store.builder()
                                                .storeId(testStoreId)
                                                .storeName("카카오프렌즈 팝업스토어 강남점")
                                                .build()
                                )
                                .build()
                ));

        given(userClient.getUserById(testUserId))
                .willReturn(ResDTO.success(
                        ResUserClientGetByIdDTOApiV1.builder()
                                .user(ResUserClientGetByIdDTOApiV1.User.builder()
                                        .userId(testUserId)
                                        .username("라이언")
                                        .slackId(testSlackId)
                                        .build())
                                .build()
                ));
    }

    // 예약 생성
    @Test
    public void testProductReservationPostSuccess() throws Exception {
        ReqProductReservationPostDTOApiV1 reqDto = ReqProductReservationPostDTOApiV1.builder()
                .quantity(1)
                .build();

        mockMvc.perform(
                RestDocumentationRequestBuilders.post("/v1/product-reservations/{productId}", testProductId)
                        .header("X-User-Id", testUserId)
                        .header("X-User-Role", "USER")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqDto))
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.jsonPath("$.code").value("000"),
                        MockMvcResultMatchers.jsonPath("$.data.productReservation.productId").value(testProductId.toString()),
                        MockMvcResultMatchers.jsonPath("$.data.productReservation.quantity").value(1)
                )
                .andDo(
                        document("상품 예약 성공",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(ResourceSnippetParameters.builder()
                                        .tag("Product Reservations v1")
                                        .summary("상품 예약")
                                        .description("""
                                                ## 상품 예약 엔드포인트 입니다.
                                                
                                                ---
                                                
                                                """)
                                        .pathParameters(
                                                parameterWithName("productId").type(SimpleType.STRING).description("상품 ID")
                                        )
                                        .requestFields(
                                                fieldWithPath("quantity").type(JsonFieldType.NUMBER).description("구매 수량")
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
        ProductReservationEntity saved = createProductReservation(ProductReservationStatus.PENDING_PICKUP);

        mockMvc.perform(
                RestDocumentationRequestBuilders.post("/v1/product-reservations/{productReservationId}/cancel", saved.getProductReservationId())
                        .header("X-User-Id", testUserId)
                        .header("X-User-Role", "USER")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.jsonPath("code").value("000"),
                        MockMvcResultMatchers.jsonPath("data.productReservation.productReservationId").value(saved.getProductReservationId().toString()),
                        MockMvcResultMatchers.jsonPath("data.productReservation.status").value("CANCELED")
                )
                .andDo(
                        document("상품 예약 취소 성공",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(ResourceSnippetParameters.builder()
                                        .tag("Product Reservations v1")
                                        .summary("상품 예약 취소")
                                        .description("""
                                                ## 상품 예약 취소 엔드포인트 입니다.
                                                
                                                ---
                                                
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
        ProductReservationEntity saved = createProductReservation(ProductReservationStatus.PENDING_PICKUP);

        mockMvc.perform(
                RestDocumentationRequestBuilders.get("/v1/product-reservations/{productReservationId}", saved.getProductReservationId())
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
                                        .tag("Product Reservations v1")
                                        .summary("상품 예약 상세 조회")
                                        .description("""
                                                ## 상품 예약 상세 조회 엔드포인트 입니다.

                                                ---

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
                                                fieldWithPath("data.productReservation.user.slackId").type(JsonFieldType.STRING).description("유저 슬랙 ID"),

                                                fieldWithPath("data.productReservation.store.storeId").type(JsonFieldType.STRING).description("스토어 ID"),
                                                fieldWithPath("data.productReservation.store.storeName").type(JsonFieldType.STRING).description("스토어 이름")
                                        )
                                        .build()
                                )
                        )
                );
    }

    // 예약 전체 조회
    @Test
    public void testProductReservationGetSuccess() throws Exception {
        createProductReservation(ProductReservationStatus.PENDING_PICKUP);

        mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/v1/product-reservations")
                                .param("page", "0")
                                .param("size", "10")
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.jsonPath("code").value("000"),
                        MockMvcResultMatchers.jsonPath("data.productReservationList").isArray(),
                        MockMvcResultMatchers.jsonPath("data.pageInfo").exists(),
                        MockMvcResultMatchers.jsonPath("data.pageInfo.size").value(10)
                )
                .andDo(
                        document("상품 예약 전체 조회 성공",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(ResourceSnippetParameters.builder()
                                        .tag("Product Reservations v1")
                                        .summary("상품 예약 전체 조회")
                                        .description("""
                                            ## 상품 예약 전체 조회 엔드포인트입니다.
                                            
                                            ---
                                            
                                            """)
                                        .queryParameters(
                                                parameterWithName("page").description("페이지 번호 (0부터 시작)").optional(),
                                                parameterWithName("size").description("페이지당 아이템 수").optional()
                                        )
                                        .responseFields(
                                                fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                                fieldWithPath("data.productReservationList[].productReservationId").type(JsonFieldType.STRING).description("상품 예약 ID"),
                                                fieldWithPath("data.productReservationList[].productId").type(JsonFieldType.STRING).description("상품 ID"),
                                                fieldWithPath("data.productReservationList[].userId").type(JsonFieldType.NUMBER).description("유저 ID"),
                                                fieldWithPath("data.productReservationList[].storeId").type(JsonFieldType.STRING).description("스토어 ID"),
                                                fieldWithPath("data.productReservationList[].quantity").type(JsonFieldType.NUMBER).description("예약 수량"),
                                                fieldWithPath("data.productReservationList[].status").type(JsonFieldType.STRING).description("예약 상태"),

                                                fieldWithPath("data.pageInfo.page").type(JsonFieldType.NUMBER).description("현재 페이지 번호"),
                                                fieldWithPath("data.pageInfo.size").type(JsonFieldType.NUMBER).description("요청한 페이지 사이즈"),
                                                fieldWithPath("data.pageInfo.totalPages").type(JsonFieldType.NUMBER).description("총 페이지 수"),
                                                fieldWithPath("data.pageInfo.totalElements").type(JsonFieldType.NUMBER).description("전체 예약 수")
                                        )
                                        .build()
                                )
                        )
                );
    }

    private ProductReservationEntity createProductReservation(ProductReservationStatus status) {
        return productReservationJpaRepository.save(
                ProductReservationEntity.builder()
                        .productId(testProductId)
                        .userId(testUserId)
                        .storeId(testStoreId)
                        .quantity(1)
                        .status(status)
                        .build()
        );
    }
}

