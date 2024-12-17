package com.realarmproduct.productNotification.domain;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProductUserNotificationHistory {
    private Long productId;
    private Long userId;
    private Integer restockRound;
    private LocalDateTime sentAt;

    public ProductUserNotificationHistory(Long productId, Long userId, Integer restockRound) {
        this.productId = productId;
        this.userId = userId;
        this.restockRound = restockRound;
        this.sentAt = LocalDateTime.now();
    }
}
