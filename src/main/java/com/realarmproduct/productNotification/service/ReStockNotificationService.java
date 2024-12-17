package com.realarmproduct.productNotification.service;

import com.realarmproduct.common.enums.NotificationStatus;
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

        if (product.isOutOfStock()) {
            throw new IllegalStateException("재고가 없는 상품입니다.");
        }

        product.incrementRestockRound();
        notificationRepository.saveProduct(product);

        ProductNotificationHistory history = new ProductNotificationHistory(productId, product.getRestockRound());
        notificationRepository.saveNotificationHistory(history);

        try {
            List<ProductUserNotification> users = notificationRepository.findAllActiveUserNotifications(productId);

            for (int start = 0; start < users.size(); start += BATCH_SIZE) {
                int end = Math.min(start + BATCH_SIZE, users.size());
                List<ProductUserNotification> batch = users.subList(start, end);

                for (ProductUserNotification user : batch) {
                    if (product.isOutOfStock()) {
                        history.changeStatus(NotificationStatus.CANCELED_BY_SOLD_OUT, user.getUserId());
                        notificationRepository.saveNotificationHistory(history);
                        return;
                    }

                    // 알림 전송
                    alarmSender.notify(user.getUserId(), productId, product.getRestockRound());

                    // 알림 히스토리 저장
                    ProductUserNotificationHistory userHistory = new ProductUserNotificationHistory(
                            productId, user.getUserId(), product.getRestockRound());
                    notificationRepository.saveUserNotificationHistory(userHistory);

                    // 진행 상태 업데이트
                    history.changeStatus(NotificationStatus.IN_PROGRESS, user.getUserId());
                    notificationRepository.saveNotificationHistory(history);

                    Thread.sleep(2);
                }
            }

            // 완료 상태 업데이트
            history.changeStatus(NotificationStatus.COMPLETED, null);
            notificationRepository.saveNotificationHistory(history);
        } catch (Exception e) {
            history.changeStatus(NotificationStatus.CANCELED_BY_ERROR, null);
            notificationRepository.saveNotificationHistory(history);
            throw new RuntimeException("알림 전송 중 오류 발생", e);
        }
    }

}
