package com.monkey.productservice.domain.entity;

import com.monkey.productservice.application.dto.request.ReqProductPostDTOApiV1;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Table(name = "p_product")
public class ProductEntity {
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

    public ProductEntity(ReqProductPostDTOApiV1.Product productDto) {
        this.storeId = productDto.getStoreId();
        this.productName = productDto.getProductName();
        this.price = productDto.getPrice();
        this.quantity = productDto.getQuantity();
    }
}
