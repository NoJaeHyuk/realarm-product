package com.realarmproduct.productNotification.service;

import com.realarmproduct.common.enums.NotificationStatus;
import com.realarmproduct.productNotification.domain.Product;
import com.realarmproduct.productNotification.domain.ProductNotificationHistory;
import com.realarmproduct.productNotification.domain.ProductUserNotification;
import com.realarmproduct.productNotification.domain.ProductUserNotificationHistory;

import java.util.List;
import java.util.Optional;

public interface ReStockNotificationRepository {
    Product findProductById(Long productId);

    void saveProduct(Product product);

    ProductNotificationHistory saveNotificationHistory(ProductNotificationHistory history);

    List<ProductUserNotification> findAllActiveUserNotifications(Long productId);

    void saveUserNotificationHistory(ProductUserNotificationHistory history);

    Optional<ProductNotificationHistory> findLatestNotificationHistory(Long productId);

    List<ProductUserNotification> findAllActiveUserNotificationsAfterUserId(Long productId, Long lastSentUserId);
}
