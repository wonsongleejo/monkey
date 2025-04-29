package com.monkey.productreservationservice.domain.service;

import java.util.UUID;

public interface ProductStockServiceV1 {
    Integer getProductStock(UUID productId);
    Integer refreshStockFromDB(UUID productId);
    void ensureStockInRedis(UUID productId);
    void decreaseStock(UUID productId, int quantity);
    void increaseStock(UUID productId, int quantity);
}
