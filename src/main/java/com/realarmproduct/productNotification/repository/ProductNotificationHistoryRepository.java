package com.realarmproduct.productNotification.repository;

import com.realarmproduct.productNotification.repository.entity.ProductNotificationHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductNotificationHistoryRepository extends JpaRepository<ProductNotificationHistoryEntity, Long> {
    ProductNotificationHistoryEntity findByProduct_ProductIdAndRestockRound(Long productId, Integer restockRound);

}
