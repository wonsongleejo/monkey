package com.monkey.storeservice.presentaion.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.monkey.storeservice.application.dto.request.ReqStoreTimeSlotPostDTOApiV1;
import com.monkey.storeservice.application.dto.request.ReqStoreTimeSlotPutDTOApiV1;
import com.monkey.storeservice.application.service.v1.StoreTimeSlotServiceApiV1;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static com.epages.restdocs.apispec.ResourceDocumentation.*;
import static com.epages.restdocs.apispec.ResourceSnippetParameters.builder;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
public class StoreTimeSlotControllerApiV1Test {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private StoreTimeSlotServiceApiV1 storeTimeSlotServiceApiV1;

  @Test
  void postTimeSlotTest() throws Exception {
    ReqStoreTimeSlotPostDTOApiV1 req = ReqStoreTimeSlotPostDTOApiV1.builder()
        .storeTimeSlot(ReqStoreTimeSlotPostDTOApiV1.StoreTimeSlot.builder()
            .slotDate(LocalDate.of(2025, 4, 14))
            .entryTime(LocalTime.of(9, 0))
            .exitTime(LocalTime.of(18, 0))
            .maxPerson(10)
            .build())
        .build();

    mockMvc.perform(RestDocumentationRequestBuilders.post("/v1/timeslots/{storeId}", UUID.randomUUID())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("000"))
        .andDo(document("시간대-등록",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            resource(builder()
                .tag("TIMESLOTS v1")
                .summary("시간대 등록")
                .description("시간대를 등록합니다.")
                .pathParameters(
                    parameterWithName("storeId").description("스토어 ID")
                )
                .requestFields(
                    fieldWithPath("storeTimeSlot.slotDate").description("날짜"),
                    fieldWithPath("storeTimeSlot.entryTime").description("입장 시간"),
                    fieldWithPath("storeTimeSlot.exitTime").description("퇴장 시간"),
                    fieldWithPath("storeTimeSlot.maxPerson").optional().description("최대 인원")
                )
                .responseFields(
                    fieldWithPath("code").description("응답 코드"),
                    fieldWithPath("message").description("응답 메시지"),
                    fieldWithPath("data.storeTimeSlot.timeSlotId").description("시간대 ID")
                )
                .build()
            )
        ));
  }

  @Test
  void putByIdTimeSlotTest() throws Exception {
    ReqStoreTimeSlotPutDTOApiV1 req = ReqStoreTimeSlotPutDTOApiV1.builder()
        .storeTimeSlot(ReqStoreTimeSlotPutDTOApiV1.StoreTimeSlot.builder()
            .slotDate(LocalDate.of(2025, 4, 14))
            .entryTime(LocalTime.of(9, 30))
            .exitTime(LocalTime.of(18, 0))
            .maxPerson(30)
            .build())
        .build();

    mockMvc.perform(RestDocumentationRequestBuilders.put("/v1/timeslots/{timeSlotId}", UUID.randomUUID())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("000"))
        .andDo(document("시간대-수정",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            resource(builder()
                .tag("TIMESLOTS v1")
                .summary("시간대 수정")
                .description("시간대를 수정합니다.")
                .pathParameters(
                    parameterWithName("timeSlotId").description("시간대 ID")
                )
                .requestFields(
                    fieldWithPath("storeTimeSlot.slotDate").description("날짜"),
                    fieldWithPath("storeTimeSlot.entryTime").description("입장 시간"),
                    fieldWithPath("storeTimeSlot.exitTime").description("퇴장 시간"),
                    fieldWithPath("storeTimeSlot.maxPerson").description("최대 인원")
                )
                .responseFields(
                    fieldWithPath("code").description("응답 코드"),
                    fieldWithPath("message").description("응답 메시지"),
                    fieldWithPath("data.slotDate").description("날짜"),
                    fieldWithPath("data.entryTime").description("입장 시간"),
                    fieldWithPath("data.exitTime").description("퇴장 시간"),
                    fieldWithPath("data.maxPerson").description("최대 인원")
                )
                .build()
            )
        ));
  }

  @Test
  void getByIdTimeSlotTest() throws Exception {
    mockMvc.perform(RestDocumentationRequestBuilders.get("/v1/timeslots/{timeSlotId}", UUID.randomUUID()))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("000"))
        .andDo(document("시간대-단건조회",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            resource(builder()
                .tag("TIMESLOTS v1")
                .summary("시간대 단건 조회")
                .description("시간대 ID로 단건 조회합니다.")
                .pathParameters(
                    parameterWithName("timeSlotId").description("시간대 ID")
                )
                .responseFields(
                    fieldWithPath("code").description("응답 코드"),
                    fieldWithPath("message").description("응답 메시지"),
                    fieldWithPath("data.storeTimeSlot.storeId").description("스토어 ID"),
                    fieldWithPath("data.storeTimeSlot.timeSlotId").description("시간대 ID"),
                    fieldWithPath("data.storeTimeSlot.slotDate").description("날짜"),
                    fieldWithPath("data.storeTimeSlot.entryTime").description("입장 시간"),
                    fieldWithPath("data.storeTimeSlot.exitTime").description("퇴장 시간"),
                    fieldWithPath("data.storeTimeSlot.maxPerson").description("최대 인원"),
                    fieldWithPath("data.storeTimeSlotsList").description("시간대 리스트 (해당 API에서는 null)").optional()
                )
                .build()
            )
        ));
  }

  @Test
  void getAllTimeSlotsTest() throws Exception {
    mockMvc.perform(RestDocumentationRequestBuilders.get("/v1/timeslots"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("000"))
        .andDo(document("시간대-전체조회",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            resource(builder()
                .tag("TIMESLOTS v1")
                .summary("시간대 전체 조회")
                .description("등록된 시간대 전체 리스트를 조회합니다.")
                .responseFields(
                    fieldWithPath("code").description("응답 코드"),
                    fieldWithPath("message").description("응답 메시지"),
                    fieldWithPath("data.storeTimeSlot").optional().description("단건 시간대 정보"),
                    fieldWithPath("data.storeTimeSlotsList[].storeId").description("스토어 ID"),
                    fieldWithPath("data.storeTimeSlotsList[].timeSlotId").description("시간대 ID"),
                    fieldWithPath("data.storeTimeSlotsList[].slotDate").description("날짜"),
                    fieldWithPath("data.storeTimeSlotsList[].entryTime").description("입장 시간"),
                    fieldWithPath("data.storeTimeSlotsList[].exitTime").description("퇴장 시간"),
                    fieldWithPath("data.storeTimeSlotsList[].maxPerson").description("최대 인원")
                )
                .build()
            )
        ));
  }
}
