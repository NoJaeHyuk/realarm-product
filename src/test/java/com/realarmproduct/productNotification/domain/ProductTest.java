package com.realarmproduct.productNotification.domain;

import com.realarmproduct.common.enums.StockStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTest {

    @Test
    @DisplayName("incrementRestockRound() 호출 시 회차가 1 증가해야 한다")
    void incrementRestockRound() {
        // Given
        Product product = new Product(1L, 1, StockStatus.IN_STOCK);

        // When
        product.incrementRestockRound();

        // Then
        assertThat(product.getRestockRound()).isEqualTo(2);
    }

    @Test
    @DisplayName("재고 상태가 OUT_OF_STOCK이면 true를 반환")
    void isOutOfStock_True() {
        // Given
        Product product = new Product(1L, 1, StockStatus.OUT_OF_STOCK);

        // When
        boolean result = product.isOutOfStock();

        // Then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("재고 상태가 OUT_OF_STOCK이 아니면 false를 반환")
    void isOutOfStock_False() {
        // Given
        Product product = new Product(1L, 1, StockStatus.IN_STOCK);

        // When
        boolean result = product.isOutOfStock();

        // Then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("changeStockStatus() 호출 시 상태가 변경되어야 한다")
    void changeStockStatus() {
        // Given
        Product product = new Product(1L, 1, StockStatus.IN_STOCK);

        // When
        product.changeStockStatus(StockStatus.OUT_OF_STOCK);

        // Then
        assertThat(product.getStockStatus()).isEqualTo(StockStatus.OUT_OF_STOCK);
    }
}
