package com.monkey.productservice.presentation.controller;

import com.monkey.commonmodule.dto.ResDTO;
import com.monkey.commonmodule.dto.ResponseCode;
import com.monkey.commonmodule.exception.CustomException;
import com.monkey.productservice.application.dto.request.ReqProductPostDTOApiV1;
import com.monkey.productservice.application.dto.request.ReqProductPutDTOApiV1;
import com.monkey.productservice.application.dto.response.ResProductGetByIdDTOApiV1;
import com.monkey.productservice.application.dto.response.ResProductGetDTOApiV1;
import com.monkey.productservice.application.dto.response.ResProductPostDTOApiV1;
import com.monkey.productservice.application.dto.response.ResProductPutDTOApiV1;
import com.monkey.productservice.domain.entity.ProductEntity;
import com.monkey.productservice.domain.repository.ProductRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/products")
public class ProductControllerApiV1 {
    private final ProductRepository productRepository;

    // 상품 등록
    @PostMapping
    public ResponseEntity<ResDTO<ResProductPostDTOApiV1>> postBy(
            @RequestBody @Valid ReqProductPostDTOApiV1 reqDto
            ) {
        ProductEntity productEntity = reqDto.getProduct().toEntity();

        ProductEntity savedProductEntity = productRepository.save(productEntity);
        ResProductPostDTOApiV1 resDto = ResProductPostDTOApiV1.of(savedProductEntity);

        return new ResponseEntity<>(ResDTO.success(resDto), HttpStatus.OK);
    }

    // 상품 수정
    @PutMapping("/{productId}")
    public ResponseEntity<ResDTO<ResProductPutDTOApiV1>> putBy(
            @PathVariable UUID productId,
            @RequestBody @Valid ReqProductPutDTOApiV1 reqDto
    ) {
        ProductEntity productEntity = getActiveProductById(productId);
        reqDto.getProduct().update(productEntity);

        ProductEntity updatedProductEntity = productRepository.save(productEntity);
        ResProductPutDTOApiV1 resDto = ResProductPutDTOApiV1.of(updatedProductEntity);

        return new ResponseEntity<>(ResDTO.success(resDto), HttpStatus.OK);
    }

    // 상품 전체 조회
    @GetMapping
    public ResponseEntity<ResDTO<ResProductGetDTOApiV1>> getBy() {
        List<ProductEntity> productList = productRepository.findAllByIsDeletedFalse();

        ResProductGetDTOApiV1 resDto = ResProductGetDTOApiV1.of(productList);
        return new ResponseEntity<>(ResDTO.success(resDto), HttpStatus.OK);
    }

    // 상품 단건 조회
    @GetMapping("/{productId}")
    public ResponseEntity<ResDTO<ResProductGetByIdDTOApiV1>> getById(@PathVariable UUID productId) {
        ProductEntity product = getActiveProductById(productId);

        ResProductGetByIdDTOApiV1 resDto = ResProductGetByIdDTOApiV1.of(product);
        return new ResponseEntity<>(ResDTO.success(resDto), HttpStatus.OK);
    }

    // 상품 삭제
    @DeleteMapping("/{productId}")
    public ResponseEntity<ResDTO<Object>> deleteBy(@PathVariable UUID productId) {
        ProductEntity productEntity = getActiveProductById(productId);
        productEntity.delete(123L);
        productRepository.save(productEntity);

        return new ResponseEntity<>(ResDTO.success(null), HttpStatus.OK);
    }

    // 존재하는 상품 검증 메서드
    private ProductEntity getActiveProductById(UUID productId) {
        return productRepository.findByIdAndIsDeletedIsFalse(productId)
                .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND));
    }
}
