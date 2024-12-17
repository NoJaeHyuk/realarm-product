package com.realarmproduct.productNotification.domain;

import lombok.Getter;

@Getter
public class ProductUserNotification {
    private final Long productId;
    private final Long userId;
    private final Boolean isActive;

    public ProductUserNotification(Long productId, Long userId, Boolean isActive) {
        this.productId = productId;
        this.userId = userId;
        this.isActive = isActive;
    }
}
