package com.monkey.productreservationservice.presentation.controller;

import com.monkey.commonmodule.dto.ResDTO;
import com.monkey.productreservationservice.application.dto.request.ReqProductReservationPostDTOApiV1;
import com.monkey.productreservationservice.application.dto.response.ResProductReservationPostDTOApiV1;
import com.monkey.productreservationservice.domain.entity.ProductReservationEntity;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/product-reservations")
public class ProductReservationControllerApiV1 {

    // 예약 생성
    @PostMapping("/{productId}")
    public ResponseEntity<ResDTO<ResProductReservationPostDTOApiV1>> postBy(
            @RequestBody @Valid ReqProductReservationPostDTOApiV1 reqDto
            ) {
        ProductReservationEntity productReservationEntity = reqDto.getProductReservation().toEntity();

        ResProductReservationPostDTOApiV1 resDto = ResProductReservationPostDTOApiV1.of(productReservationEntity);

        return new ResponseEntity<>(ResDTO.success(resDto), HttpStatus.OK);
    }
}
