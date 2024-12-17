package com.realarmproduct.productNotification.repository.entity;

import com.realarmproduct.common.BaseTimeEntity;
import com.realarmproduct.common.enums.NotificationStatus;
import com.realarmproduct.productNotification.domain.ProductNotificationHistory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ProductNotificationHistory")
public class ProductNotificationHistoryEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    private Integer restockRound;

    @Enumerated(EnumType.STRING)
    private NotificationStatus notificationStatus;

    private Long lastSentUserId;

    @Builder
    private ProductNotificationHistoryEntity(Long id, ProductEntity product, Integer restockRound, NotificationStatus notificationStatus, Long lastSentUserId) {
        this.id = id;
        this.product = product;
        this.restockRound = restockRound;
        this.notificationStatus = notificationStatus;
        this.lastSentUserId = lastSentUserId;
    }

    public static ProductNotificationHistoryEntity fromDomain(ProductNotificationHistory domain, ProductEntity productEntity) {
        return ProductNotificationHistoryEntity.builder()
                .product(productEntity)
                .restockRound(domain.getRestockRound())
                .notificationStatus(domain.getNotificationStatus())
                .lastSentUserId(domain.getLastSentUserId())
                .build();
    }

    public void changeStatus(NotificationStatus status, Long lastUserId) {
        if (status == null) {
            throw new IllegalArgumentException("NotificationStatus는 null일 수 없습니다.");
        }
        this.notificationStatus = status;
        this.lastSentUserId = lastUserId;
    }
}
