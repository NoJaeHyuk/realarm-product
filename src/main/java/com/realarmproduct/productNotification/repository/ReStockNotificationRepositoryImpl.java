package com.realarmproduct.productNotification.repository;

import com.realarmproduct.common.enums.NotificationStatus;
import com.realarmproduct.productNotification.domain.Product;
import com.realarmproduct.productNotification.domain.ProductNotificationHistory;
import com.realarmproduct.productNotification.domain.ProductUserNotification;
import com.realarmproduct.productNotification.domain.ProductUserNotificationHistory;
import com.realarmproduct.productNotification.repository.entity.ProductEntity;
import com.realarmproduct.productNotification.repository.entity.ProductNotificationHistoryEntity;
import com.realarmproduct.productNotification.repository.entity.ProductUserNotificationEntity;
import com.realarmproduct.productNotification.repository.entity.ProductUserNotificationHistoryEntity;
import com.realarmproduct.productNotification.service.ReStockNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ReStockNotificationRepositoryImpl implements ReStockNotificationRepository {
    private final ProductRepository productRepository;
    private final ProductNotificationHistoryRepository historyRepository;
    private final ProductUserNotificationRepository userNotificationRepository;
    private final ProductUserNotificationHistoryRepository userHistoryRepository;

    @Override
    public Product findProductById(Long productId) {
        ProductEntity entity = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
        return new Product(entity.getProductId(), entity.getRestockRound(), entity.getStockStatus());
    }

    @Override
    @Transactional
    public void saveProduct(Product product) {
        ProductEntity entity = ProductEntity.fromDomain(product);
        productRepository.save(entity);
    }

    @Override
    public ProductNotificationHistory saveNotificationHistory(ProductNotificationHistory history) {
        ProductEntity productEntity = productRepository.findById(history.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        ProductNotificationHistoryEntity entity = ProductNotificationHistoryEntity.fromDomain(history, productEntity);
        historyRepository.save(entity);
        return history;
    }

    @Override
    public List<ProductUserNotification> findAllActiveUserNotifications(Long productId) {
        List<ProductUserNotificationEntity> entities = userNotificationRepository.findAllByProduct_ProductIdAndIsActiveTrue(productId);
        return entities.stream()
                .map(entity -> new ProductUserNotification(entity.getProduct().getProductId(), entity.getUserId(), entity.getIsActive()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void saveUserNotificationHistory(ProductUserNotificationHistory history) {
        ProductEntity productEntity = productRepository.findById(history.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        ProductUserNotificationHistoryEntity entity = ProductUserNotificationHistoryEntity.fromDomain(history, productEntity);
        userHistoryRepository.save(entity);
    }


    @Override
    public Optional<ProductNotificationHistory> findLatestNotificationHistory(Long productId) {
        return historyRepository.findFirstByProduct_ProductIdOrderByIdDesc(productId)
                .map(entity -> new ProductNotificationHistory(
                        productId,
                        entity.getRestockRound(),
                        entity.getNotificationStatus(),
                        entity.getLastSentUserId()
                ));
    }

    @Override
    public List<ProductUserNotification> findAllActiveUserNotificationsAfterUserId(Long productId, Long lastSentUserId) {
        List<ProductUserNotificationEntity> entities =
                userNotificationRepository.findAllByProduct_ProductIdAndIsActiveTrueAndUserIdGreaterThanOrderByUserIdAsc(
                        productId, lastSentUserId);

        return entities.stream()
                .map(entity -> new ProductUserNotification(entity.getProduct().getProductId(), entity.getUserId(), entity.getIsActive()))
                .collect(Collectors.toList());
    }

}
