package com.monkey.storeservice.domain.article.entity;

import com.monkey.commonmodule.entity.BaseEntity;
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
public class StoreEntity extends BaseEntity {

  @Id
  @Column(name = "store_id", nullable = false, unique = true)
  private UUID storeId;

  @Column(name = "store_name", length = 255, nullable = false)
  private String storeName;

  @Column(name = "description", length = 255, nullable = false)
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(name = "open_status", nullable = false)
  private OpenStatus openStatus;

  @Column(name = "start_date", nullable = false)
  private LocalDate startDate;

  @Column(name = "end_date", nullable = false)
  private LocalDate endDate;

  @Column(name = "start_time", nullable = false)
  private LocalTime startTime;

  @Column(name = "end_time", nullable = false)
  private LocalTime endTime;

  @Column(name = "total_person", nullable = false)
  private Integer totalPerson;

  public enum OpenStatus {
    OPEN, CLOSED
  }
}
