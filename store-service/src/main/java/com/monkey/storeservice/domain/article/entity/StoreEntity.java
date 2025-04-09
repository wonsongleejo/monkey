package com.monkey.storeservice.domain.article.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "p_stores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreEntity {

  @Id
  @Column(name = "storeId", nullable = false, unique = true)
  private UUID storeId;

  @Column(name = "storeName", length = 255, nullable = false)
  private String storeName;

  @Column(name = "description", length = 255, nullable = false)
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(name = "openStatus", nullable = false)
  private OpenStatus openStatus;

  @Column(name = "startDate", nullable = false)
  private LocalDate startDate;

  @Column(name = "endDate", nullable = false)
  private LocalDate endDate;

  @Column(name = "startTime", nullable = false)
  private LocalTime startTime;

  @Column(name = "endTime", nullable = false)
  private LocalTime endTime;

  @Column(name = "totalPerson", nullable = false)
  private Integer totalPerson;

  public enum OpenStatus {
    OPEN, CLOSED
  }
}
