package com.monkey.productreservationservice.application.validator.v2;

import com.monkey.common_module.dto.ResponseCode;
import com.monkey.common_module.exception.CustomException;
import com.monkey.productreservationservice.domain.repository.ProductReservationRepository;
import com.monkey.productreservationservice.domain.service.ProductStockServiceV1;
import com.monkey.productreservationservice.infrastructure.feignclient.ProductFeignClientApiV1;
import com.monkey.productreservationservice.infrastructure.feignclient.StoreReservationFeignClientApiV1;
import com.monkey.productreservationservice.infrastructure.feignclient.dto.response.ResProductClientGetByIdDTOApiV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductReservationValidatorV2 {
    private final ProductFeignClientApiV1 productClient;
    private final StoreReservationFeignClientApiV1 storeReservationClient;
    private final ProductReservationRepository productReservationRepository;
    private final ProductStockServiceV1 productStockService;

    // 상품 예약 요청 시 전체 검증 메서드
    public ResProductClientGetByIdDTOApiV1.Product validateReservationRequest(UUID productId, long userId, int quantity) {
        var product = validateProduct(productId); // 유효한 상품 확인

        validateStock(productId, quantity); // 재고 확인
        validatePurchaseLimit(quantity, product.getPurchaseLimitPerUser()); // 구매수량 제한
        validateStoreMember(userId, product.getStore().getStoreId()); // 스토어 예약여부 확인
        validateNotDuplicate(userId, productId); // 중복 예약 확인

        return product;
    }

    // 존재하는 상품 확인
    private ResProductClientGetByIdDTOApiV1.Product validateProduct(UUID productId) {
        try {
            var productResponse = productClient.getProductById(productId);
            var product = productResponse != null ? productResponse.getData().getProduct() : null;
            log.info("product storeId {}", product.getStore().getStoreId());
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
    private void validateStock(UUID productId, int requestQuantity) {
        int stock = productStockService.getProductStock(productId);

        if (requestQuantity > stock) {
            throw new CustomException(ResponseCode.PRODUCT_OUT_OF_STOCK);
        }
    }

    // 구매 수량 제한
    private void validatePurchaseLimit(int requestQuantity, int purchaseLimitPerUser) {
        if (requestQuantity > purchaseLimitPerUser) {
            throw new CustomException(ResponseCode.PRODUCT_PURCHASE_LIMIT_EXCEEDED);
        }
    }

    // 스토어 예약여부 확인
    private void validateStoreMember(long userId, UUID storeId) {
        try {
            var storeResponse = storeReservationClient.getReservationsByStoreId(storeId, userId);

            if (storeResponse == null || storeResponse.getData() == null ||
                    storeResponse.getData().getStoreReservationList() == null) {
                throw new CustomException(ResponseCode.NOT_STORE_MEMBER);
            }

            boolean matched = storeResponse.getData().getStoreReservationList().stream()
                    .anyMatch(res ->
                            res.getUser() != null &&
                                    res.getUser().getUserId() == userId &&
                                    res.getTimeSlot() != null &&
                                    res.getTimeSlot().getStore() != null &&
                                    res.getTimeSlot().getStore().getStoreId().equals(storeId)
                    );

            if (!matched) {
                throw new CustomException(ResponseCode.NOT_STORE_MEMBER);
            }

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(ResponseCode.STORE_RESERVATION_FEIGN_CLIENT_ERROR);
        }
    }

    // 중복 예약 방지
    private void validateNotDuplicate(long userId, UUID productId) {
        if (productReservationRepository.existsByUserIdAndProductIdAndIsDeletedFalse(userId, productId)) {
            throw new CustomException(ResponseCode.DUPLICATED_REQUEST);
        }
    }
}