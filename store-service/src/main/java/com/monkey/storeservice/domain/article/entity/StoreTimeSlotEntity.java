package com.monkey.storeservice.domain.article.entity;

import com.monkey.commonmodule.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "p_store_timeslots")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreTimeSlotEntity extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "timeslot_id", nullable = false)
  private UUID timeslotId;

  @Column(name = "store_id", nullable = false)
  private UUID storeId;

  @Column(name = "slot_date", nullable = false)
  private LocalDate slotDate;

  @Column(name = "entry_time", nullable = false)
  private LocalTime entryTime;

  @Column(name = "exit_time", nullable = false)
  private LocalTime exitTime;

  @Column(name = "max_person", nullable = false)
  private Integer maxPerson;
}
