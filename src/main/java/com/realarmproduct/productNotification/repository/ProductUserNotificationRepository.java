package com.realarmproduct.productNotification.repository;

import com.realarmproduct.productNotification.repository.entity.ProductUserNotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductUserNotificationRepository extends JpaRepository<ProductUserNotificationEntity, Long> {
    List<ProductUserNotificationEntity> findAllByProduct_ProductIdAndIsActiveTrue(Long productId);
    List<ProductUserNotificationEntity> findAllByProduct_ProductIdAndIsActiveTrueAndUserIdGreaterThanOrderByUserIdAsc(
            Long productId, Long lastSentUserId);

}
