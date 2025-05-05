package com.monkey.productservice.application.event;

import com.monkey.productservice.application.event.dto.ProductStockIncreaseFailPayloadV1;

public interface ProductEventProduceV1 {
    void increaseStockFailed(ProductStockIncreaseFailPayloadV1 build);
}