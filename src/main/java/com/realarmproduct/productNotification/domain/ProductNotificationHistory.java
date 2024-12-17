package com.realarmproduct.productNotification.domain;

import com.realarmproduct.common.enums.NotificationStatus;
import lombok.Getter;

@Getter
public class ProductNotificationHistory {
    private Long id;
    private Long productId;
    private Integer restockRound;
    private NotificationStatus notificationStatus;
    private Long lastSentUserId;

    public ProductNotificationHistory(Long productId, Integer restockRound, NotificationStatus status, Long lastSentUserId) {
        this.productId = productId;
        this.restockRound = restockRound;
        this.notificationStatus = status;
        this.lastSentUserId = lastSentUserId;
    }

    public void updateStatus(NotificationStatus status, Long lastSentUserId) {
        this.notificationStatus = status;
        this.lastSentUserId = lastSentUserId;
    }
}
