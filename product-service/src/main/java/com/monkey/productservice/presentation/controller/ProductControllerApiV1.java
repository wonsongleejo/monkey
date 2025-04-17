package com.monkey.productservice.presentation.controller;

import com.monkey.common_module.dto.ResDTO;
import com.monkey.productservice.application.dto.request.ReqProductPostDTOApiV1;
import com.monkey.productservice.application.dto.request.ReqProductPutDTOApiV1;
import com.monkey.productservice.application.dto.response.ResProductGetByIdDTOApiV1;
import com.monkey.productservice.application.dto.response.ResProductGetDTOApiV1;
import com.monkey.productservice.application.dto.response.ResProductPostDTOApiV1;
import com.monkey.productservice.application.dto.response.ResProductPutDTOApiV1;
import com.monkey.productservice.application.service.ProductServiceApiV1;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/products")
public class ProductControllerApiV1 {
    private final ProductServiceApiV1 productServiceApiV1;

    // 상품 등록
    @PostMapping
    public ResponseEntity<ResDTO<ResProductPostDTOApiV1>> postBy(
            @RequestBody @Valid ReqProductPostDTOApiV1 reqDto
            ) {
        ResProductPostDTOApiV1 resDto = productServiceApiV1.postBy(reqDto);
        return new ResponseEntity<>(ResDTO.success(resDto), HttpStatus.OK);
    }

    // 상품 수정
    @PutMapping("/{productId}")
    public ResponseEntity<ResDTO<ResProductPutDTOApiV1>> putBy(
            @PathVariable UUID productId,
            @RequestBody @Valid ReqProductPutDTOApiV1 reqDto
    ) {
        ResProductPutDTOApiV1 resDto = productServiceApiV1.putBy(productId, reqDto);
        return new ResponseEntity<>(ResDTO.success(resDto), HttpStatus.OK);
    }

    // 상품 전체 조회
    @GetMapping
    public ResponseEntity<ResDTO<ResProductGetDTOApiV1>> getBy() {
        ResProductGetDTOApiV1 resDto = productServiceApiV1.getBy();
        return new ResponseEntity<>(ResDTO.success(resDto), HttpStatus.OK);
    }

    // 상품 단건 조회
    @GetMapping("/{productId}")
    public ResponseEntity<ResDTO<ResProductGetByIdDTOApiV1>> getById(@PathVariable UUID productId) {
        ResProductGetByIdDTOApiV1 resDto = productServiceApiV1.getById(productId);
        return new ResponseEntity<>(ResDTO.success(resDto), HttpStatus.OK);
    }

    // 상품 삭제
    @DeleteMapping("/{productId}")
    public ResponseEntity<ResDTO<Object>> deleteBy(@PathVariable UUID productId) {
        productServiceApiV1.deleteById(productId);
        return new ResponseEntity<>(ResDTO.success(null), HttpStatus.OK);
    }
}
