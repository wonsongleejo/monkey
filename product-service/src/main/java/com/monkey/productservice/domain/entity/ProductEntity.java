package com.monkey.productservice.domain.entity;

import com.monkey.common_module.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "p_product")
@Builder
public class ProductEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID productId;

    @Column(nullable = false)
    private UUID storeId;

    @Column(nullable = false, length = 100)
    private String productName;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Integer purchaseLimitPerUser;

    public void decreaseStock(int quantity) {
        if(quantity <= 0) {
            throw new IllegalArgumentException("감소할 수량은 1개 이상이어야 합니다.");
        }
        this.quantity -= quantity;
    }
}
