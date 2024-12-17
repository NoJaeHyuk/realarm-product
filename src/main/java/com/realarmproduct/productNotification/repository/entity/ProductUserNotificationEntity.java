package com.realarmproduct.productNotification.repository.entity;

import com.realarmproduct.common.BaseTimeEntity;
import com.realarmproduct.productNotification.domain.ProductUserNotification;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ProductUserNotification")
public class ProductUserNotificationEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    private Long userId;

    private Boolean isActive;

    @Builder
    private ProductUserNotificationEntity(Long id, ProductEntity product, Long userId, Boolean isActive) {
        this.id = id;
        this.product = product;
        this.userId = userId;
        this.isActive = isActive;
    }

    public static ProductUserNotificationEntity fromDomain(ProductUserNotification domain, ProductEntity productEntity) {
        return ProductUserNotificationEntity.builder()
                .product(productEntity)
                .userId(domain.getUserId())
                .isActive(domain.getIsActive())
                .build();
    }
}
