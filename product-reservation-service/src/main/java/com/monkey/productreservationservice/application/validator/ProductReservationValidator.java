package com.monkey.productreservationservice.application.validator;

import com.monkey.common_module.dto.ResponseCode;
import com.monkey.common_module.exception.CustomException;
import com.monkey.productreservationservice.domain.repository.ProductReservationRepository;
import com.monkey.productreservationservice.infrastructure.feignclient.ProductFeignClientApiV1;
import com.monkey.productreservationservice.infrastructure.feignclient.StoreReservationFeignClientApiV1;
import com.monkey.productreservationservice.infrastructure.feignclient.dto.response.ResProductClientGetByIdDTOApiV1;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProductReservationValidator {
    private final ProductFeignClientApiV1 productClient;
    private final StoreReservationFeignClientApiV1 storeReservationClient;
    private final ProductReservationRepository productReservationRepository;

    // 존재하는 상품 확인
    public ResProductClientGetByIdDTOApiV1.Product validateProduct(UUID productId) {
        try {
            var productResponse = productClient.getProductById(productId);
            var product = productResponse != null ? productResponse.getData().getProduct() : null;

            if (product == null) throw new CustomException(ResponseCode.PRODUCT_NOT_FOUND);
            if (product.getStore() == null || product.getStore().getStoreId() == null)
                throw new CustomException(ResponseCode.STORE_NOT_FOUND);

            return product;
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(ResponseCode.PRODUCT_FEIGN_CLIENT_ERROR);
        }
    }

    // 상품 재고 확인
    public void validateStock(int requestQuantity, int quantity) {
        if (requestQuantity > quantity) {
            throw new CustomException(ResponseCode.PRODUCT_OUT_OF_STOCK);
        }
    }

    // 구매 수량 제한
    public void validatePurchaseLimit(int requestQuantity, int purchaseLimitPerUser) {
        if (requestQuantity > purchaseLimitPerUser) {
            throw new CustomException(ResponseCode.PRODUCT_PURCHASE_LIMIT_EXCEEDED);
        }
    }

    // 스토어 예약여부 확인
    public void validateStoreMember(long userId, UUID storeId) {
        try {
            var memberResponse = storeReservationClient.getReservationsByUserIdAndStoreId(userId, storeId);

            if(memberResponse == null ||
                    memberResponse.getData() == null ||
                    memberResponse.getData().getStoreReservationList() == null ||
                    memberResponse.getData().getStoreReservationList().isEmpty()) {
                throw new CustomException(ResponseCode.NOT_STORE_MEMBER);
            }
            boolean hasValidUser = memberResponse.getData().getStoreReservationList().stream()
                    .anyMatch(reservation -> reservation.getUser() != null && reservation.getUser().getUserId() == userId);

            if (!hasValidUser) {
                throw new CustomException(ResponseCode.NOT_STORE_MEMBER);
            }
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(ResponseCode.STORE_RESERVATION_FEIGN_CLIENT_ERROR);
        }
    }

    // 중복 예약 방지
    public void validateNotDuplicate(long userId, UUID productId) {
        if (productReservationRepository.existsByUserIdAndProductIdAndIsDeletedFalse(userId, productId)) {
            throw new CustomException(ResponseCode.DUPLICATED_REQUEST);
        }
    }
}

