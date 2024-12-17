package com.realarmproduct.productNotification.service;

import com.realarmproduct.common.enums.NotificationStatus;
import com.realarmproduct.common.enums.StockStatus;
import com.realarmproduct.productNotification.domain.Product;
import com.realarmproduct.productNotification.domain.ProductNotificationHistory;
import com.realarmproduct.productNotification.domain.ProductUserNotification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class ReStockNotificationServiceTest {

    private ReStockNotificationRepository notificationRepository;
    private AlarmSender alarmSender;
    private ReStockNotificationService reStockNotificationService;

    @BeforeEach
    void setUp() {
        notificationRepository = mock(ReStockNotificationRepository.class);
        alarmSender = mock(AlarmSender.class);
        reStockNotificationService = new ReStockNotificationService(notificationRepository, alarmSender);
    }

    @Test
    @DisplayName("재입고 알림서비스에 실행되는 메서드가 정상 호출되는지 확인한다.")
    void sendReStockNotifications_Success() throws Exception {
        // Given
        Long productId = 1L;
        Product product = new Product(productId, 1, StockStatus.OUT_OF_STOCK);
        List<ProductUserNotification> userNotifications = Arrays.asList(
                new ProductUserNotification(productId, 101L, true),
                new ProductUserNotification(productId, 102L, true)
        );

        when(notificationRepository.findProductById(productId)).thenReturn(product);
        when(notificationRepository.findAllActiveUserNotifications(productId)).thenReturn(userNotifications);

        // When
        reStockNotificationService.sendReStockNotifications(productId);

        // Then
        verify(notificationRepository, times(1)).saveProduct(any(Product.class));
        verify(notificationRepository, times(1)).saveNotificationHistory(any(ProductNotificationHistory.class));
        verify(notificationRepository, times(2)).saveUserNotificationHistory(any());
        verify(alarmSender, times(2)).notify(anyLong(), eq(productId), anyInt());
        verify(notificationRepository, times(3)).saveNotificationHistory(any());
    }

    @Test
    @DisplayName("재고가 없는 제품의 알람을 실행 시 예외를 발생시킨다.")
    void sendReStockNotifications_Failure_OutOfStock() {
        // Given
        Long productId = 1L;
        Product product = new Product(productId, 1, StockStatus.OUT_OF_STOCK);

        when(notificationRepository.findProductById(productId)).thenReturn(product);

        // When & Then
        assertThatThrownBy(() -> reStockNotificationService.sendReStockNotifications(productId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("재고가 없는 상품입니다.");
    }

    @Test
    @DisplayName("중간에 재고가 소진된 경우 알림 전송 중단 및 상태 변경")
    void sendReStockNotifications_CanceledBySoldOut() throws Exception {
        // Given
        Long productId = 1L;
        Product product = new Product(productId, 1, StockStatus.IN_STOCK);
        List<ProductUserNotification> userNotifications = Arrays.asList(
                new ProductUserNotification(productId, 101L, true),
                new ProductUserNotification(productId, 102L, true)
        );

        when(notificationRepository.findProductById(productId)).thenReturn(product);
        when(notificationRepository.findAllActiveUserNotifications(productId)).thenReturn(userNotifications);

        // 재고 상태를 중간에 변경
        // 101L알림 보낼때 상태가 변경되니 다음 102L때에 알림 중단 발생
        doAnswer(invocation -> {
            product.changeStockStatus(StockStatus.OUT_OF_STOCK);
            return null;
        }).when(alarmSender).notify(eq(101L), eq(productId), anyInt());

        // ArgumentCaptor를 설정하여 상태를 확인
        ArgumentCaptor<ProductNotificationHistory> historyCaptor = ArgumentCaptor.forClass(ProductNotificationHistory.class);

        // When
        reStockNotificationService.sendReStockNotifications(productId);

        // Then
        verify(notificationRepository, atLeastOnce()).saveNotificationHistory(historyCaptor.capture());

        ProductNotificationHistory capturedHistory = historyCaptor.getValue();
        assertThat(capturedHistory.getNotificationStatus()).isEqualTo(NotificationStatus.CANCELED_BY_SOLD_OUT);
        assertThat(capturedHistory.getLastSentUserId()).isEqualTo(102L);
    }

    @Test
    @DisplayName("알림 전송 중 오류 발생 시 상태가 CANCELED_BY_ERROR로 변경된다.")
    void sendReStockNotifications_Failure_ErrorDuringNotification() throws Exception {
        // Given
        Long productId = 1L;
        Product product = new Product(productId, 1, StockStatus.IN_STOCK);
        List<ProductUserNotification> userNotifications = Arrays.asList(
                new ProductUserNotification(productId, 101L, true)
        );

        when(notificationRepository.findProductById(productId)).thenReturn(product);
        when(notificationRepository.findAllActiveUserNotifications(productId)).thenReturn(userNotifications);

        doThrow(new RuntimeException("알림 전송 실패"))
                .when(alarmSender).notify(anyLong(), eq(productId), anyInt());

        // When & Then
        assertThatThrownBy(() -> reStockNotificationService.sendReStockNotifications(productId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("알림 전송 중 오류 발생");

        verify(notificationRepository, times(2)).saveNotificationHistory(argThat(history ->
                history.getNotificationStatus() == NotificationStatus.CANCELED_BY_ERROR));
    }
}

