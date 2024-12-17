package com.realarmproduct.productNotification.domain;

import com.realarmproduct.common.enums.NotificationStatus;
import lombok.Getter;

@Getter
public class ProductNotificationHistory {
    private Long id;
    private final Long productId;
    private final Integer restockRound;
    private NotificationStatus notificationStatus;
    private Long lastSentUserId;

    public ProductNotificationHistory(Long productId, Integer restockRound) {
        this.productId = productId;
        this.restockRound = restockRound;
        this.notificationStatus = NotificationStatus.IN_PROGRESS;
        this.lastSentUserId = null;
    }

    public void updateStatus(NotificationStatus status, Long lastSentUserId) {
        this.notificationStatus = status;
        this.lastSentUserId = lastSentUserId;
    }

    public void changeStatus(NotificationStatus status, Long userId) {
        this.notificationStatus = status;

        if (status == NotificationStatus.IN_PROGRESS || status == NotificationStatus.CANCELED_BY_SOLD_OUT) {
            this.lastSentUserId = userId;
        } else {
            this.lastSentUserId = null; // 다른 상태에서는 lastSentUserId를 초기화
        }
    }
}
