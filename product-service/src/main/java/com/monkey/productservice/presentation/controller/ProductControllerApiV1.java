package com.monkey.productservice.presentation.controller;

import com.monkey.commonmodule.dto.ResDTO;
import com.monkey.commonmodule.dto.ResponseCode;
import com.monkey.productservice.application.dto.request.ReqProductPostDTOApiV1;
import com.monkey.productservice.application.dto.request.ReqProductPutDTOApiV1;
import com.monkey.productservice.application.dto.response.ResProductPostDTOApiV1;
import com.monkey.productservice.application.dto.response.ResProductPutDTOApiV1;
import com.monkey.productservice.domain.entity.ProductEntity;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/products")
public class ProductControllerApiV1 {

    // 상품 등록
    @PostMapping
    public ResponseEntity<ResDTO<ResProductPostDTOApiV1>> postBy(
            @RequestBody @Valid ReqProductPostDTOApiV1 reqDto
            ) {
        ProductEntity productEntity = reqDto.getProduct().toEntity();

        ResProductPostDTOApiV1 resDto = ResProductPostDTOApiV1.of(productEntity);

        return new ResponseEntity<>(ResDTO.success(resDto), ResponseCode.SUCCESS.getStatus());
    }

    // 상품 수정
    @PutMapping("/{producId}")
    public ResponseEntity<ResDTO<ResProductPutDTOApiV1>> putBy(
            @PathVariable UUID productId,
            @RequestBody @Valid ReqProductPutDTOApiV1 reqDto
    ) {
        // 임시 데이터 (나중에 productId로 조회한 후 수정하는 로직으로 변경)
        ProductEntity productEntity = ProductEntity.builder()
                .productId(productId)
                .storeId(UUID.randomUUID())
                .productName("바나나 인형")
                .price(0)
                .quantity(0)
                .build();

        reqDto.getProduct().update(productEntity);
        ResProductPutDTOApiV1 resDto = ResProductPutDTOApiV1.of(productEntity);

        return new ResponseEntity<>(ResDTO.success(resDto), ResponseCode.SUCCESS.getStatus());

    }

}
