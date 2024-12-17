package com.realarmproduct.productNotification.repository.entity;

import com.realarmproduct.common.BaseTimeEntity;
import com.realarmproduct.common.enums.StockStatus;
import com.realarmproduct.productNotification.domain.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product")
public class ProductEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private Integer restockRound;

    @Enumerated(EnumType.STRING)
    private StockStatus stockStatus;

    @Builder
    private ProductEntity(Long productId, Integer restockRound, StockStatus stockStatus) {
        this.productId = productId;
        this.restockRound = restockRound;
        this.stockStatus = stockStatus;
    }

    public static ProductEntity fromDomain(Product product) {
        return ProductEntity.builder()
                .productId(product.getProductId())
                .restockRound(product.getRestockRound())
                .stockStatus(product.getStockStatus())
                .build();
    }
}
