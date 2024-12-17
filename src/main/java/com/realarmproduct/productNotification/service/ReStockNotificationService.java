package com.realarmproduct.productNotification.service;

import com.realarmproduct.common.enums.NotificationStatus;
import com.realarmproduct.common.enums.StockStatus;
import com.realarmproduct.productNotification.domain.Product;
import com.realarmproduct.productNotification.domain.ProductNotificationHistory;
import com.realarmproduct.productNotification.domain.ProductUserNotification;
import com.realarmproduct.productNotification.domain.ProductUserNotificationHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReStockNotificationService {
    private final ReStockNotificationRepository notificationRepository;
    private final AlarmSender alarmSender;

    private static final int BATCH_SIZE = 500; // 한 번에 처리할 사용자 수

    @Transactional
    public void sendReStockNotifications(Long productId) {
        Product product = notificationRepository.findProductById(productId);
        if (product.getStockStatus() == StockStatus.IN_STOCK) {
            throw new IllegalStateException("이미 재고가 있는 상품입니다.");
        }

        product.incrementRestockRound();
        notificationRepository.saveProduct(product);

        ProductNotificationHistory history = new ProductNotificationHistory(productId, product.getRestockRound(), NotificationStatus.IN_PROGRESS, null);
        notificationRepository.saveNotificationHistory(history);

        try {
            List<ProductUserNotification> users = notificationRepository.findAllActiveUserNotifications(productId);

            // 500개씩 나누어 배치 처리
            for (int start = 0; start < users.size(); start += BATCH_SIZE) {
                int end = Math.min(start + BATCH_SIZE, users.size());
                List<ProductUserNotification> batch = users.subList(start, end);

                for (ProductUserNotification user : batch) {
                    if (product.getStockStatus() == StockStatus.IN_STOCK) {
                        notificationRepository.updateNotificationHistoryStatus(productId, product.getRestockRound(), NotificationStatus.CANCELED_BY_SOLD_OUT, user.getUserId());
                        return;
                    }

                    alarmSender.notify(user.getUserId(), productId, product.getRestockRound());
                    ProductUserNotificationHistory userHistory = new ProductUserNotificationHistory(productId, user.getUserId(), product.getRestockRound());
                    notificationRepository.saveUserNotificationHistory(userHistory);
                    notificationRepository.updateNotificationHistoryStatus(productId, product.getRestockRound(), NotificationStatus.IN_PROGRESS, user.getUserId());
                    Thread.sleep(2); // 2ms 대기
                }
            }

            notificationRepository.updateNotificationHistoryStatus(productId, product.getRestockRound(), NotificationStatus.COMPLETED, null);
        } catch (Exception e) {
            notificationRepository.updateNotificationHistoryStatus(productId, product.getRestockRound(), NotificationStatus.CANCELED_BY_ERROR, null);
            throw new RuntimeException("알림 전송 중 오류 발생", e);
        }
    }

}
