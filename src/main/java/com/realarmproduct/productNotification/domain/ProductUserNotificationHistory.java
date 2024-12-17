package com.realarmproduct.productNotification.domain;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProductUserNotificationHistory {
    private final Long productId;
    private final Long userId;
    private final Integer restockRound;
    private final LocalDateTime sentAt;

    public ProductUserNotificationHistory(Long productId, Long userId, Integer restockRound) {
        this.productId = productId;
        this.userId = userId;
        this.restockRound = restockRound;
        this.sentAt = LocalDateTime.now();
    }
}
