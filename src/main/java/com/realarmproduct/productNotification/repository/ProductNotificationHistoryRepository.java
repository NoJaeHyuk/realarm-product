package com.realarmproduct.productNotification.repository;

import com.realarmproduct.productNotification.repository.entity.ProductNotificationHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductNotificationHistoryRepository extends JpaRepository<ProductNotificationHistoryEntity, Long> {
    ProductNotificationHistoryEntity findByProduct_ProductIdAndRestockRound(Long productId, Integer restockRound);
    Optional<ProductNotificationHistoryEntity> findFirstByProduct_ProductIdOrderByIdDesc(Long productId);


}
