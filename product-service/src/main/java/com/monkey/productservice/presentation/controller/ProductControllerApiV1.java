package com.monkey.productservice.presentation.controller;

import com.monkey.commonmodule.dto.ResDTO;
import com.monkey.productservice.application.dto.request.ReqProductPostDTOApiV1;
import com.monkey.productservice.application.dto.response.ResProductPostDTOApiV1;
import com.monkey.productservice.domain.entity.ProductEntity;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/products")
public class ProductControllerApiV1 {

    // 상품 등록
    @PostMapping
    public ResponseEntity<ResDTO<ResProductPostDTOApiV1>> createProduct(
            @RequestBody @Valid ReqProductPostDTOApiV1 request
            ) {
        ProductEntity productEntity = new ProductEntity(request.getProduct());

        ResProductPostDTOApiV1 response = ResProductPostDTOApiV1.of(productEntity);

        return ResponseEntity.ok(ResDTO.success(response));

    }

}
