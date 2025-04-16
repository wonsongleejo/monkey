package com.monkey.productreservationservice.domain.entity;

import com.monkey.common_module.entity.BaseEntity;
import com.monkey.productreservationservice.domain.vo.ProductReservationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "p_product_reservation")
@Builder
public class ProductReservationEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID productReservationId;

    @Column(nullable = false)
    private UUID productId;

    @Column(nullable = false)
    private long userId;

    @Column(nullable = false)
    private UUID storeId;

    @Column(nullable = false)
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductReservationStatus status;
}
