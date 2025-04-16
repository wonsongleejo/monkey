package com.monkey.storeservice.presentation;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.SimpleType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monkey.storeservice.application.dto.request.ReqStorePostDtoApiV1;
import com.monkey.storeservice.application.dto.request.ReqStorePutDtoApiV1;
import com.monkey.storeservice.domain.article.entity.StoreEntity;
import com.monkey.storeservice.domain.article.entity.StoreEntity.OpenStatus;
import com.monkey.storeservice.infrastructure.persistence.StoreJpaRepository;
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

import java.time.LocalDate;
import java.time.LocalTime;
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
public class StoreControllerApiV1Test {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper; // 👉 JSON 변환을 위한 객체

  @Autowired
  private StoreJpaRepository storeJpaRepository;

  // 스토어 생성 테스트
  @Test
  public void postByStoreTest() throws Exception {
    ReqStorePostDtoApiV1 reqDto = ReqStorePostDtoApiV1.builder()
        .store(ReqStorePostDtoApiV1.Store.builder()
            .storeName("루피 팝업")
            .description("루피 굿즈 팝업")
            .startDate(LocalDate.parse("2025-04-09"))
            .endDate(LocalDate.parse("2025-04-16"))
            .startTime(LocalTime.parse("10:00"))
            .endTime(LocalTime.parse("18:00"))
            .openStatus(OpenStatus.CLOSED)
            .totalPersonCount(50)
            .build())
        .build();

    mockMvc.perform(
            RestDocumentationRequestBuilders.post("/v1/stores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reqDto))
        )
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("000"))
        .andDo(document("스토어 등록 성공",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            resource(ResourceSnippetParameters.builder()
                .tag("STORES v1")
                .summary("스토어 등록")
                .description("팝업스토어 등록 엔드포인트")
                .requestFields(
                    fieldWithPath("store.storeName").type(JsonFieldType.STRING).description("스토어 이름"),
                    fieldWithPath("store.description").type(JsonFieldType.STRING).description("스토어 설명"),
                    fieldWithPath("store.startDate").type(JsonFieldType.STRING).description("시작일"),
                    fieldWithPath("store.endDate").type(JsonFieldType.STRING).description("종료일"),
                    fieldWithPath("store.startTime").type(JsonFieldType.STRING).description("시작 시간"),
                    fieldWithPath("store.endTime").type(JsonFieldType.STRING).description("종료 시간"),
                    fieldWithPath("store.totalPersonCount").type(JsonFieldType.NUMBER).description("총 예약 가능 인원"),
                    fieldWithPath("store.openStatus").type(JsonFieldType.STRING).description("스토어 상태(OPEN, CLOSED)")
                )
                .responseFields(
                    fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                    fieldWithPath("data.store.storeId").type(JsonFieldType.STRING).optional().description("스토어 ID")
                )
                .build()
            )
        ));
  }

  // 스토어 수정 테스트
  @Test
  public void putByIdStoreTest() throws Exception {
    // 저장
    StoreEntity saved = storeJpaRepository.save(
        StoreEntity.builder()
            .storeId(UUID.randomUUID())
            .storeName("루피 팝업")
            .description("루피 굿즈")
            .startDate(LocalDate.parse("2025-04-01"))
            .endDate(LocalDate.parse("2025-04-10"))
            .startTime(LocalTime.parse("10:00"))
            .endTime(LocalTime.parse("18:00"))
            .totalPersonCount(50)
            .openStatus(OpenStatus.CLOSED)
            .build()
    );

    ReqStorePutDtoApiV1 reqDto = ReqStorePutDtoApiV1.builder()
        .store(ReqStorePutDtoApiV1.Store.builder()
            .storeName("오징어게임 팝업")
            .description("오징어게임 굿즈")
            .startDate(LocalDate.parse("2025-04-11"))
            .endDate(LocalDate.parse("2025-04-20"))
            .startTime(LocalTime.parse("11:00"))
            .endTime(LocalTime.parse("19:00"))
            .totalPersonCount(100)
            .openStatus(OpenStatus.OPEN)
            .build())
        .build();

    mockMvc.perform(
            RestDocumentationRequestBuilders.put("/v1/stores/{storeId}", saved.getStoreId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reqDto))
        )
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("000"))
        .andDo(document("스토어 수정 성공",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            resource(ResourceSnippetParameters.builder()
                .tag("STORES v1")
                .summary("스토어 수정")
                .description("팝업스토어 수정 엔드포인트")
                .pathParameters(
                    parameterWithName("storeId").type(SimpleType.STRING).description("스토어 ID")
                )
                .requestFields(
                    fieldWithPath("store.storeName").type(JsonFieldType.STRING).description("스토어 이름"),
                    fieldWithPath("store.description").type(JsonFieldType.STRING).description("스토어 설명"),
                    fieldWithPath("store.startDate").type(JsonFieldType.STRING).description("시작일"),
                    fieldWithPath("store.endDate").type(JsonFieldType.STRING).description("종료일"),
                    fieldWithPath("store.startTime").type(JsonFieldType.STRING).description("시작 시간"),
                    fieldWithPath("store.endTime").type(JsonFieldType.STRING).description("종료 시간"),
                    fieldWithPath("store.totalPersonCount").type(JsonFieldType.NUMBER).description("총 예약 가능 인원"),
                    fieldWithPath("store.openStatus").type(JsonFieldType.STRING).description("스토어 상태(OPEN, CLOSED)")
                )
                .responseFields(
                    fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                    fieldWithPath("data.storeId").type(JsonFieldType.STRING).optional().description("스토어 ID"),
                    fieldWithPath("data.storeName").type(JsonFieldType.STRING).description("스토어 이름"),
                    fieldWithPath("data.description").type(JsonFieldType.STRING).description("스토어 설명"),
                    fieldWithPath("data.openStatus").type(JsonFieldType.STRING).description("스토어 상태"),
                    fieldWithPath("data.startDate").type(JsonFieldType.STRING).description("시작일"),
                    fieldWithPath("data.endDate").type(JsonFieldType.STRING).description("종료일"),
                    fieldWithPath("data.startTime").type(JsonFieldType.STRING).description("시작 시간"),
                    fieldWithPath("data.endTime").type(JsonFieldType.STRING).description("종료 시간"),
                    fieldWithPath("data.totalPersonCount").type(JsonFieldType.NUMBER).description("총 예약 가능 인원")
                )
                .build()
            )
        ));
  }

  // 스토어 단건 조회 테스트
  @Test
  public void getByIdStoreTest() throws Exception {
    StoreEntity saved = storeJpaRepository.save(
        StoreEntity.builder()
            .storeId(UUID.randomUUID())
            .storeName("루피 팝업스토어")
            .description("굿즈 팝업")
            .startDate(LocalDate.parse("2025-04-10"))
            .endDate(LocalDate.parse("2025-05-01"))
            .startTime(LocalTime.parse("10:00"))
            .endTime(LocalTime.parse("18:00"))
            .totalPersonCount(150)
            .openStatus(OpenStatus.OPEN)
            .build()
    );

    mockMvc.perform(
            RestDocumentationRequestBuilders.get("/v1/stores/{storeId}", saved.getStoreId())
        )
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("000"))
        .andDo(document("스토어 단건 조회 성공",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            resource(ResourceSnippetParameters.builder()
                .tag("STORES v1")
                .summary("스토어 단건 조회")
                .description("스토어 단건 조회 엔드포인트")
                .pathParameters(
                    parameterWithName("storeId").type(SimpleType.STRING).description("스토어 ID")
                )
                .responseFields(
                    fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                    fieldWithPath("data.store.storeId").type(JsonFieldType.STRING).description("스토어 ID"),
                    fieldWithPath("data.store.storeName").type(JsonFieldType.STRING).description("스토어 이름"),
                    fieldWithPath("data.store.description").type(JsonFieldType.STRING).description("스토어 설명"),
                    fieldWithPath("data.store.startDate").type(JsonFieldType.STRING).description("시작일"),
                    fieldWithPath("data.store.endDate").type(JsonFieldType.STRING).description("종료일"),
                    fieldWithPath("data.store.startTime").type(JsonFieldType.STRING).description("시작 시간"),
                    fieldWithPath("data.store.endTime").type(JsonFieldType.STRING).description("종료 시간"),
                    fieldWithPath("data.store.totalPersonCount").type(JsonFieldType.NUMBER).description("예약 가능 인원"),
                    fieldWithPath("data.store.openStatus").type(JsonFieldType.STRING).description("스토어 상태"),
                    fieldWithPath("data.storeList").type(JsonFieldType.NULL).optional().description("스토어 리스트 (해당 테스트에선 null)")
                )
                .build()
            )
        ));
  }
  @Test
  void getAllStoreTest() throws Exception {
    mockMvc.perform(RestDocumentationRequestBuilders.get("/v1/stores"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("000"))
        .andDo(document("스토어 전체 조회 성공",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            resource(ResourceSnippetParameters.builder()
                .tag("STORES v1")
                .summary("스토어 전체 조회")
                .description("팝업스토어 전체 조회 엔드포인트")
                .responseFields(
                    fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                    fieldWithPath("data.store").type(JsonFieldType.NULL).optional().description("단일 스토어 정보 (해당 요청에서는 null)"),
                    fieldWithPath("data.storeList[].storeId").type(JsonFieldType.STRING).description("스토어 ID"),
                    fieldWithPath("data.storeList[].storeName").type(JsonFieldType.STRING).description("스토어 이름"),
                    fieldWithPath("data.storeList[].description").type(JsonFieldType.STRING).description("스토어 설명"),
                    fieldWithPath("data.storeList[].openStatus").type(JsonFieldType.STRING).description("스토어 상태"),
                    fieldWithPath("data.storeList[].startDate").type(JsonFieldType.STRING).description("시작일"),
                    fieldWithPath("data.storeList[].endDate").type(JsonFieldType.STRING).description("종료일"),
                    fieldWithPath("data.storeList[].startTime").type(JsonFieldType.STRING).description("시작 시간"),
                    fieldWithPath("data.storeList[].endTime").type(JsonFieldType.STRING).description("종료 시간"),
                    fieldWithPath("data.storeList[].totalPersonCount").type(JsonFieldType.NUMBER).description("예약 가능 인원")
                )
                .build()
            )
        ));
  }

}
