package com.monkey.productreservationservice.application.validator;

import com.monkey.common_module.dto.ResponseCode;
import com.monkey.common_module.exception.CustomException;
import com.monkey.productreservationservice.infrastructure.feignclient.ProductFeignClientApiV1;
import com.monkey.productreservationservice.infrastructure.feignclient.StoreFeignClientApiV1;
import com.monkey.productreservationservice.infrastructure.feignclient.UserFeignClientApiV1;
import com.monkey.productreservationservice.infrastructure.feignclient.dto.response.ResProductClientGetByIdDTOApiV1;
import com.monkey.productreservationservice.infrastructure.feignclient.dto.response.ResStoreClientGetByIdDTOApiV1;
import com.monkey.productreservationservice.infrastructure.feignclient.dto.response.ResUserClientGetByIdDTOApiV1;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProductReservationReadValidator {
    private final ProductFeignClientApiV1 productClient;
    private final StoreFeignClientApiV1 storeClient;
    private final UserFeignClientApiV1 userClient;

    // 상품 확인
    public ResProductClientGetByIdDTOApiV1.Product validateProduct(UUID productId) {
        try {
            var productRes = productClient.getProductById(productId);
            var product = productRes != null ? productRes.getData().getProduct() : null;

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

    // 스토어 확인
    public ResStoreClientGetByIdDTOApiV1.Store validateStore(UUID storeId) {
        try {
            var storeRes = storeClient.getStoreById(storeId);
//            var store = storeResponse != null ? storeResponse.getData() : null;
            var store = storeRes.getData() != null ? storeRes.getData().getStore() : null;

            if (store == null) throw new CustomException(ResponseCode.STORE_NOT_FOUND);
            return store;

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(ResponseCode.STORE_FEIGN_CLIENT_ERROR);
        }
    }

    // 유저 확인
    public ResUserClientGetByIdDTOApiV1.User validateUser(long userId) {
        try {
            var userResponse = userClient.getUserById(userId);
            var user = userResponse != null ? userResponse.getData() : null;

            if (user == null || user.getUser() == null) {
                throw new CustomException(ResponseCode.USER_NOT_FOUND);
            }
            return user.getUser();
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(ResponseCode.USER_FEIGN_CLIENT_ERROR);
        }
    }
}
