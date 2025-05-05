package com.monkey.productservice.application.service.v3;

import java.util.UUID;

public interface ProductServiceApiV3 {
    void increaseStock(UUID productId, int quantity);
}