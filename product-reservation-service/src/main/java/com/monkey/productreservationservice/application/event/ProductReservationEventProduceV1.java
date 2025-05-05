package com.monkey.productreservationservice.application.event;

import com.monkey.productreservationservice.application.event.dto.ProductStockIncreasePayloadV1;

public interface ProductReservationEventProduceV1 {
    void increaseStock(ProductStockIncreasePayloadV1 build);
}
