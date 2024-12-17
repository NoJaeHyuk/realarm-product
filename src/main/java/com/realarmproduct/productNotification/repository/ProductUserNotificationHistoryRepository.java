package com.realarmproduct.productNotification.repository;

import com.realarmproduct.productNotification.repository.entity.ProductUserNotificationHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductUserNotificationHistoryRepository extends JpaRepository<ProductUserNotificationHistoryEntity, Long> {
}
