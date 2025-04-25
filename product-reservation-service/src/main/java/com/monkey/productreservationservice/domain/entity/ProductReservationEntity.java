package com.monkey.productreservationservice.domain.entity;

import com.monkey.common_module.dto.ResponseCode;
import com.monkey.common_module.entity.BaseEntity;
import com.monkey.common_module.exception.CustomException;
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
public class ProductReservationEntity extends BaseEntity {

    @Builder
    public ProductReservationEntity(UUID productId, Long userId, UUID storeId, Integer quantity,
                                    ProductReservationStatus status) {
        this.productId = productId;
        this.userId = userId;
        this.storeId = storeId;
        this.quantity = quantity;
        this.status = status;
    }

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

    public void cancel(long userId) {
        if(this.getIsDeleted() || this.status == ProductReservationStatus.CANCELED || this.status == ProductReservationStatus.PICKED_UP) {
            throw new CustomException(ResponseCode.DUPLICATED_REQUEST);
        }
        this.status = ProductReservationStatus.CANCELED;
        this.delete(userId);
    }

    public void fail(long userId) {
        if (this.getIsDeleted() || this.status == ProductReservationStatus.CANCELED || this.status == ProductReservationStatus.PICKED_UP || this.status == ProductReservationStatus.FAILED) {
            throw new CustomException(ResponseCode.DUPLICATED_REQUEST);
        }
        this.status = ProductReservationStatus.FAILED;
        this.delete(userId);
    }
}
