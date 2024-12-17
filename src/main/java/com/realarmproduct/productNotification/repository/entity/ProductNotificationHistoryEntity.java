package com.realarmproduct.productNotification.repository.entity;

import com.realarmproduct.common.BaseTimeEntity;
import com.realarmproduct.common.enums.NotificationStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
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
}
