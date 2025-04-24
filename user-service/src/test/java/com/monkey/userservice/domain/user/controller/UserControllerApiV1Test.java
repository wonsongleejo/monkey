package com.monkey.userservice.domain.user.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.SimpleType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monkey.common_module.dto.ResDTO;
import com.monkey.userservice.application.dto.request.ReqUserPutDTOApiV1;
import com.monkey.userservice.infrastructure.client.ProductReservationFeignClientApiV1;
import com.monkey.userservice.infrastructure.client.StoreReservationFeignClientApiV1;
import com.monkey.userservice.infrastructure.client.dto.ResProductReservationClientGetDTOApiV1;
import com.monkey.userservice.infrastructure.client.dto.ResStoreReservationClientGetDTOApiV1;
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
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class UserControllerApiV1Test {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StoreReservationFeignClientApiV1 storeReservationFeignClientApiV1;

    @MockitoBean
    private ProductReservationFeignClientApiV1 productReservationFeignClientApiV1;

    @Autowired
    private ObjectMapper objectMapper;

    // 회원 전제조회
    @Test
    public void testUserGetSuccess() throws Exception {
        mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/v1/users")
                                .header("X-User-Id", "1")
                                .header("X-User-Role", "MASTER")
                                .header("X-User-Name", "testmaster")
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.jsonPath("code").value("000")
                )
                .andDo(
                        document(
                                "USER 회원 전체조회 성공",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(
                                        ResourceSnippetParameters.builder()
                                                .tag("USER v1")
                                                .summary("USER 리스트 조회")
                                                .description("""
                                                        ## USER 회원 전체조회 엔드포인트 입니다.

                                                        ---

                                                        정렬 기준을 지정할 수 있습니다. 기본 정렬은 id,desc입니다.

                                                        ex) sort=asc

                                                        페이징이 가능합니다. 기본 페이지 번호는 0번입니다.
                                                        페이지 당 조회 개수는 10, 30, 50로만 설정 가능합니다.(기본 조회 개수는 10입니다.)

                                                        ex) size=30

                                                        ex) page=1

                                                        ex) page=2&size=10

                                                        """)
                                                .queryParameters(
                                                        parameterWithName("sort").type(SimpleType.STRING).optional().description("정렬 기준"),
                                                        parameterWithName("page").type(SimpleType.NUMBER).optional().description("페이지 번호"),
                                                        parameterWithName("size").type(SimpleType.NUMBER).optional().description("페이지 당 조회 개수")
                                                )
                                                .build()
                                )
                        )
                );
    }

    // 회원 상세조회(마스터 용)
    @Test
    public void testUserGetByIdSuccess() throws Exception {
        mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/v1/users/master/{id}", 1)
                                .header("X-User-Id", "1")
                                .header("X-User-Role", "MASTER")
                                .header("X-User-Name", "testmaster")
                ) //
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.jsonPath("code").value("000")
                )
                .andDo(
                        document(
                                "MASTER의 USER 회원 상세조회 성공",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(
                                        ResourceSnippetParameters.builder()
                                                .tag("USER v1")
                                                .summary("MASTER가 USER 상세 조회")
                                                .description("""
                                                ## MASTER가 USER 회원을 상세조회하는 엔드포인트 입니다.
                                                """)
                                                .build()
                                )
                        )
                );
    }


    // 회원 상세조회(일반 유저 용)
    @Test
    public void testUserGetBySuccess() throws Exception {
        mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/v1/users/details", 1)
                                .header("X-User-Id", "1")
                                .header("X-User-Role", "USER")
                                .header("X-User-Name", "testuser1")
                ) //
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.jsonPath("code").value("000")
                )
                .andDo(
                        document(
                                "USER 회원 상세조회 성공",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(
                                        ResourceSnippetParameters.builder()
                                                .tag("USER v1")
                                                .summary("USER 상세 조회")
                                                .description("""
                                                ## USER 회원 상세조회 엔드포인트 입니다.
                                                """)
                                                .build()
                                )
                        )
                );
    }

    // 회원 정보 수정
    @Test
    public void testUserPutByIdSuccess() throws Exception {
        ReqUserPutDTOApiV1 reqDto = ReqUserPutDTOApiV1.builder()
                .user(
                        ReqUserPutDTOApiV1.User.builder()
                                .password("testMod1234!@")
                                .slackId("slackIdMod")
                                .build()
                )
                .build();

        String reqDtoJson = objectMapper.writeValueAsString(reqDto);
//
        mockMvc.perform(
                        RestDocumentationRequestBuilders.put("/v1/users/{id}", 1)
                                .header("X-User-Id", "1")
                                .header("X-User-Role", "MASTER")
                                .header("X-User-Name", "testmaster")
                                .content(reqDtoJson)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.jsonPath("code").value("000")
                )
                .andDo(
                        document("USER 회원 정보 수정",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(ResourceSnippetParameters.builder()
                                        .tag("USER v1")
                                        .summary("USER 정보 수정")
                                        .description("""
                                                ## USER 회원 정보 수정 엔드포인트 입니다.

                                                ---

                                                password, slackId를 입력받아 회원정보를 수정합니다.
                                                """)
                                        .requestFields(
                                                fieldWithPath("user.password").type(JsonFieldType.STRING).description("사용자 비밀번호"),
                                                fieldWithPath("user.slackId").type(JsonFieldType.STRING).description("사용자 슬랙 아이디")
                                        )
                                        .build()
                                )
                        )
                );
    }

    // 회원 탈퇴
    @Test
    public void testUserDeleteByIdSuccess() throws Exception {
        mockMvc.perform(
                        RestDocumentationRequestBuilders.delete("/v1/users/delete", 1)
                                .header("X-User-Id", "1")
                                .header("X-User-Role", "USER")
                                .header("X-User-Name", "testuser1")
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.jsonPath("code").value("000")
                )
                .andDo(
                        document(
                                "USER 회원 탈퇴 성공",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(
                                        ResourceSnippetParameters.builder()
                                                .tag("USER v1")
                                                .summary("USER 탈퇴")
                                                .description("""
                                                ## USER 회원 탈퇴 엔드포인트 입니다.
                                                """)
                                                .build()
                                )

                        )
                );
    }

    // 팝업 스토어 예약 내역 조회
    @Test
    public void tesStoreReservationGetByIdSuccess() throws Exception {
        // given: FeignClient에서 반환할 DTO 세팅
        ResStoreReservationClientGetDTOApiV1.StoreReservation.TimeSlot.Store store =
                ResStoreReservationClientGetDTOApiV1.StoreReservation.TimeSlot.Store.builder()
                        .storeId(UUID.randomUUID())
                        .build();

        ResStoreReservationClientGetDTOApiV1.StoreReservation.TimeSlot timeSlot =
                ResStoreReservationClientGetDTOApiV1.StoreReservation.TimeSlot.builder()
                        .store(store)
                        .date(LocalDate.of(2025, 4, 25))
                        .entryTime(LocalTime.of(10, 0))
                        .exitTime(LocalTime.of(11, 0))
                        .build();

        ResStoreReservationClientGetDTOApiV1.StoreReservation.User user =
                ResStoreReservationClientGetDTOApiV1.StoreReservation.User.builder()
                        .userId(1L)
                        .userName("홍길동")
                        .build();

        ResStoreReservationClientGetDTOApiV1.StoreReservation reservation =
                ResStoreReservationClientGetDTOApiV1.StoreReservation.builder()
                        .storeReservationId(UUID.randomUUID())
                        .visitStatus("VISITED")
                        .timeSlot(timeSlot)
                        .user(user)
                        .build();

        ResStoreReservationClientGetDTOApiV1 resDto =
                ResStoreReservationClientGetDTOApiV1.builder()
                        .storeReservationList(List.of(reservation))
                        .build();

        // when: FeignClient Mock 결과 설정
        given(storeReservationFeignClientApiV1.getStoreReservations(1L))
                .willReturn(
                        ResDTO.<ResStoreReservationClientGetDTOApiV1>builder()
                                .data(resDto)
                                .build()
                );

        // then: 실제 API 호출
        mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/v1/users/{id}/store-reservations",1)
                                .header("X-User-Id", "1")
                                .header("X-User-Role", "USER")
                                .header("X-User-Name", "testuser1")
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.jsonPath("code").value("000")
                )
                .andDo(
                        document(
                                "USER 팝업 스토어 예약내역 조회 성공",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(
                                        ResourceSnippetParameters.builder()
                                                .tag("USER v1")
                                                .summary("USER 팝업 스토어 예약 내역 조회")
                                                .description("""
                                                        ## USER 팝업 스토어 예약 내역 조회 엔드포인트 입니다.

                                                        ---

                                                        StoreReservation 서비스로 FeignClient 요청을 보내서 예약 내역을 받습니다.
                                                        """)
                                                .build()
                                )
                        )
                );
    }

    // 한정 상품 예약 내역 조회
    @Test
    public void tesProductReservationGetByIdSuccess() throws Exception {
        // given: FeignClient에서 반환할 DTO 세팅
        ResProductReservationClientGetDTOApiV1.ProductReservation.Product product =
                ResProductReservationClientGetDTOApiV1.ProductReservation.Product.builder()
                        .productId(UUID.randomUUID())
                        .productName("동글동글 마카롱")
                        .build();

        ResProductReservationClientGetDTOApiV1.ProductReservation.Store store =
                ResProductReservationClientGetDTOApiV1.ProductReservation.Store.builder()
                        .storeId(UUID.randomUUID())
                        .storeName("인기만점 마카롱 맛집")
                        .quantity(100)
                        .receivedStatus("PENDING_PICKUP")
                        .build();

        ResProductReservationClientGetDTOApiV1.ProductReservation reservation =
                ResProductReservationClientGetDTOApiV1.ProductReservation.builder()
                        .productReservationId(UUID.randomUUID())
                        .product(product)
                        .store(store)
                        .build();

        ResProductReservationClientGetDTOApiV1 resDto =
                ResProductReservationClientGetDTOApiV1.builder()
                        .productReservationList(List.of(reservation))
                        .build();

        // when: FeignClient Mock 결과 설정
        given(productReservationFeignClientApiV1.getProductReservations(1L))
                .willReturn(
                        ResDTO.<ResProductReservationClientGetDTOApiV1>builder()
                                .data(resDto)
                                .build()
                );

        mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/v1/users/{id}/product-reservations",1)
                                .header("X-User-Id", "1")
                                .header("X-User-Role", "USER")
                                .header("X-User-Name", "testuser1")
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.jsonPath("code").value("000")
                )
                .andDo(
                        document(
                                "USER 한정 상품 예약내역 조회 성공",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(
                                        ResourceSnippetParameters.builder()
                                                .tag("USER v1")
                                                .summary("USER 한정 상품 예약 내역 조회")
                                                .description("""
                                                        ## USER 한정 상품 예약 내역 조회 엔드포인트 입니다.

                                                        ---

                                                        ProductReservation 서비스로 FeignClient 요청을 보내서 예약 내역을 받습니다.
                                                        """)
                                                .build()
                                )
                        )
                );
    }

}
