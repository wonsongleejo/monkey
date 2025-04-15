package com.monkey.storereservationservice.domain.StoreReservation.entity;

import com.monkey.commonmodule.entity.BaseEntity;
import com.monkey.storereservationservice.domain.StoreReservation.vo.StoreReservationStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Table(name = "p_store_reservation")
@SQLRestriction("deleted_at is null")
public class StoreReservationEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID storeReservationId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private UUID timeSlotId;

    @Column(nullable = false)
    private Integer personCount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StoreReservationStatus status;

    public static StoreReservationEntity createStoreReservation(
            UUID timeSlotId,
            Long userId,
            Integer personCount,
            StoreReservationStatus status
    ) {
        return StoreReservationEntity.builder()
                .timeSlotId(timeSlotId)
                .userId(userId)
                .personCount(personCount)
                .status(status)
                .build();
    }

    // 예약 상태 변경
    public void changeStatus(StoreReservationStatus status) {
        this.status = status;
    }
}