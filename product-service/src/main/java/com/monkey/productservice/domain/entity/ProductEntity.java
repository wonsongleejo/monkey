package com.monkey.productservice.domain.entity;

import com.monkey.commonmodule.entity.BaseEntity;
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

    // 엔티티는 DTO랑 직접적으로 연결되면 안됨. 도메인 계층은 순수해야 함
//    public ProductEntity(ReqProductPostDTOApiV1.Product productDto) {
//        this.storeId = productDto.getStoreId();
//        this.productName = productDto.getProductName();
//        this.price = productDto.getPrice();
//        this.quantity = productDto.getQuantity();
//    }
}
