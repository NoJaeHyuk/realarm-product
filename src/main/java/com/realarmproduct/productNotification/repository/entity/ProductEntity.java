package com.realarmproduct.productNotification.repository.entity;

import com.realarmproduct.common.BaseTimeEntity;
import com.realarmproduct.common.enums.StockStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
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
}
