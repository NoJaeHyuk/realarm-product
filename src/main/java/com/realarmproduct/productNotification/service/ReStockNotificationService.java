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

    public void resendReStockNotifications(Long productId) {
        Product product = notificationRepository.findProductById(productId);

        if (product.isOutOfStock()) {
            throw new IllegalStateException("재고가 없는 상품입니다.");
        }

        ProductNotificationHistory history = notificationRepository.findLatestNotificationHistory(productId)
                .orElseThrow(() -> new IllegalStateException("재입고 알림 기록이 존재하지 않습니다."));

        if (history.getNotificationStatus() == NotificationStatus.COMPLETED) {
            throw new IllegalStateException("이미 모든 알림이 성공적으로 전송되었습니다.");
        }

        Long lastSentUserId = history.getLastSentUserId();
        List<ProductUserNotification> users = notificationRepository.findAllActiveUserNotificationsAfterUserId(productId, lastSentUserId);

        try {
            for (ProductUserNotification user : users) {
                if (product.isOutOfStock()) {
                    history.changeStatus(NotificationStatus.CANCELED_BY_SOLD_OUT, user.getUserId());
                    notificationRepository.saveNotificationHistory(history);
                    return;
                }

                alarmSender.notify(user.getUserId(), productId, product.getRestockRound());

                ProductUserNotificationHistory userHistory = new ProductUserNotificationHistory(
                        productId, user.getUserId(), product.getRestockRound());
                notificationRepository.saveUserNotificationHistory(userHistory);

                history.changeStatus(NotificationStatus.IN_PROGRESS, user.getUserId());
                notificationRepository.saveNotificationHistory(history);

                Thread.sleep(2); // 2ms 대기
            }

            history.changeStatus(NotificationStatus.COMPLETED, null);
            notificationRepository.saveNotificationHistory(history);

        } catch (Exception e) {
            history.changeStatus(NotificationStatus.CANCELED_BY_ERROR, null);
            notificationRepository.saveNotificationHistory(history);
            throw new RuntimeException("알림 재전송 중 오류 발생", e);
        }
    }
}
