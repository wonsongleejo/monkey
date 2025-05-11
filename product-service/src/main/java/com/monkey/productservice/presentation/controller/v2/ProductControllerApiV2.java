package com.monkey.productservice.presentation.controller.v2;

import com.monkey.common_module.dto.ResDTO;
import com.monkey.productservice.application.dto.response.ResProductGetDTOApiV1;
import com.monkey.productservice.application.service.v4.ProductServiceApiV4;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v2/products")
@RequiredArgsConstructor
@Slf4j
public class ProductControllerApiV2 {
    private final ProductServiceApiV4 productServiceApiV4;

    @GetMapping
    public ResponseEntity<ResDTO<ResProductGetDTOApiV1>> getBy(Pageable pageable) {
        ResProductGetDTOApiV1 resDto = productServiceApiV4.getBy(pageable);
        return new ResponseEntity<>(ResDTO.success(resDto), HttpStatus.OK);
    }
}
