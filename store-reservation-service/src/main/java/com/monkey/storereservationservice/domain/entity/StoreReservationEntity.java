package com.monkey.storereservationservice.domain.entity;

import com.monkey.common_module.entity.BaseEntity;
import com.monkey.storereservationservice.domain.vo.StoreReservationStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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
        StoreReservationEntity entity = new StoreReservationEntity();
        entity.timeSlotId = timeSlotId;
        entity.userId = userId;
        entity.personCount = personCount;
        entity.status = status;
        return entity;
    }

    // 예약 상태 변경
    public void changeStatus(StoreReservationStatus status) {
        this.status = status;
    }
}