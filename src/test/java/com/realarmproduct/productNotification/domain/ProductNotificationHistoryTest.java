package com.realarmproduct.productNotification.domain;

import com.realarmproduct.common.enums.NotificationStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductNotificationHistoryTest {

    @Test
    @DisplayName("초기 생성 시 상태는 IN_PROGRESS이고 lastSentUserId는 null이다")
    void constructor_InitialStatus() {
        // Given
        Long productId = 1L;
        Integer restockRound = 1;

        // When
        ProductNotificationHistory history = new ProductNotificationHistory(productId, restockRound);

        // Then
        assertThat(history.getNotificationStatus()).isEqualTo(NotificationStatus.IN_PROGRESS);
        assertThat(history.getLastSentUserId()).isNull();
        assertThat(history.getProductId()).isEqualTo(productId);
        assertThat(history.getRestockRound()).isEqualTo(restockRound);
    }

    @Test
    @DisplayName("IN_PROGRESS 상태에서는 lastSentUserId가 설정된다")
    void changeStatus_InProgress() {
        // Given
        ProductNotificationHistory history = new ProductNotificationHistory(1L, 1);

        // When
        history.changeStatus(NotificationStatus.IN_PROGRESS, 101L);

        // Then
        assertThat(history.getNotificationStatus()).isEqualTo(NotificationStatus.IN_PROGRESS);
        assertThat(history.getLastSentUserId()).isEqualTo(101L);
    }

    @Test
    @DisplayName("CANCELED_BY_SOLD_OUT 상태에서는 lastSentUserId가 설정된다")
    void changeStatus_CanceledBySoldOut() {
        // Given
        ProductNotificationHistory history = new ProductNotificationHistory(1L, 1);

        // When
        history.changeStatus(NotificationStatus.CANCELED_BY_SOLD_OUT, 102L);

        // Then
        assertThat(history.getNotificationStatus()).isEqualTo(NotificationStatus.CANCELED_BY_SOLD_OUT);
        assertThat(history.getLastSentUserId()).isEqualTo(102L);
    }

    @Test
    @DisplayName("COMPLETED 상태에서는 lastSentUserId가 null로 초기화된다")
    void changeStatus_Completed() {
        // Given
        ProductNotificationHistory history = new ProductNotificationHistory(1L, 1);

        // When
        history.changeStatus(NotificationStatus.COMPLETED, 103L);

        // Then
        assertThat(history.getNotificationStatus()).isEqualTo(NotificationStatus.COMPLETED);
        assertThat(history.getLastSentUserId()).isNull();
    }

    @Test
    @DisplayName("CANCELED_BY_ERROR 상태에서는 lastSentUserId가 null로 초기화된다")
    void changeStatus_CanceledByError() {
        // Given
        ProductNotificationHistory history = new ProductNotificationHistory(1L, 1);

        // When
        history.changeStatus(NotificationStatus.CANCELED_BY_ERROR, 104L);

        // Then
        assertThat(history.getNotificationStatus()).isEqualTo(NotificationStatus.CANCELED_BY_ERROR);
        assertThat(history.getLastSentUserId()).isNull();
    }
}

