package com.realarmproduct.productNotification.domain;

import com.realarmproduct.common.enums.StockStatus;
import lombok.Getter;

@Getter
public class Product {
    private final Long productId;
    private Integer restockRound;
    private final StockStatus stockStatus;

    public Product(Long productId, Integer restockRound, StockStatus stockStatus) {
        this.productId = productId;
        this.restockRound = restockRound;
        this.stockStatus = stockStatus;
    }

    public void incrementRestockRound() {
        this.restockRound++;
    }

    public boolean isOutOfStock() {
        return this.stockStatus == StockStatus.OUT_OF_STOCK;
    }
}
