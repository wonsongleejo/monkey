package com.monkey.productservice.presentation.controller;

import com.monkey.commonmodule.dto.ResDTO;
import com.monkey.productservice.application.dto.request.ReqProductPostDTOApiV1;
import com.monkey.productservice.application.dto.request.ReqProductPutDTOApiV1;
import com.monkey.productservice.application.dto.response.ResProductPostDTOApiV1;
import com.monkey.productservice.application.dto.response.ResProductPutDTOApiV1;
import com.monkey.productservice.domain.entity.ProductEntity;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/products")
public class ProductControllerApiV1 {

    // 상품 등록
    @PostMapping
    public ResponseEntity<ResDTO<ResProductPostDTOApiV1>> createProduct(
            @RequestBody @Valid ReqProductPostDTOApiV1 reqDto
            ) {
        ProductEntity productEntity = reqDto.getProduct().toEntity();

        ResProductPostDTOApiV1 resDto = ResProductPostDTOApiV1.of(productEntity);

        return ResponseEntity.ok(ResDTO.success(resDto));
    }

}
