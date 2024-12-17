package com.realarmproduct.productNotification.repository.entity;

import com.realarmproduct.common.BaseTimeEntity;
import com.realarmproduct.productNotification.domain.ProductUserNotificationHistory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ProductUserNotificationHistory")
public class ProductUserNotificationHistoryEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    private Long userId;

    private Integer restockRound;

    private LocalDateTime sentAt;

    @Builder
    private ProductUserNotificationHistoryEntity(Long id, ProductEntity product, Long userId, Integer restockRound, LocalDateTime sentAt) {
        this.id = id;
        this.product = product;
        this.userId = userId;
        this.restockRound = restockRound;
        this.sentAt = sentAt;
    }

    public static ProductUserNotificationHistoryEntity fromDomain(ProductUserNotificationHistory domain, ProductEntity productEntity) {
        return ProductUserNotificationHistoryEntity.builder()
                .product(productEntity)
                .userId(domain.getUserId())
                .restockRound(domain.getRestockRound())
                .sentAt(domain.getSentAt())
                .build();
    }
}
