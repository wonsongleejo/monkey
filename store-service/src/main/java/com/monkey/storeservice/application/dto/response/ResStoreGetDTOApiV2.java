package com.monkey.storeservice.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.monkey.storeservice.domain.entity.StoreEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResStoreGetDTOApiV2 {

  private List<Store> stores;
  private int pageNumber;
  private int pageSize;
  private long totalElements;
  private int totalPages;

  public static ResStoreGetDTOApiV2 of(Page<StoreEntity> storePage) {
    return ResStoreGetDTOApiV2.builder()
        .stores(storePage.getContent().stream().map(Store::from).toList())
        .pageNumber(storePage.getNumber())
        .pageSize(storePage.getSize())
        .totalElements(storePage.getTotalElements())
        .totalPages(storePage.getTotalPages())
        .build();
  }

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Store {
    private UUID storeId;
    private Long storeManagerId;
    private String storeName;
    private String description;
    private StoreEntity.OpenStatus openStatus;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime startTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime endTime;
    private Integer totalPersonCount;

    public static Store from(StoreEntity storeEntity) {
      return Store.builder()
          .storeId(storeEntity.getStoreId())
          .storeManagerId(storeEntity.getStoreManagerId())
          .storeName(storeEntity.getStoreName())
          .description(storeEntity.getDescription())
          .openStatus(storeEntity.getOpenStatus())
          .startDate(storeEntity.getStartDate())
          .endDate(storeEntity.getEndDate())
          .startTime(storeEntity.getStartTime())
          .endTime(storeEntity.getEndTime())
          .totalPersonCount(storeEntity.getTotalPersonCount())
          .build();
    }
  }
}