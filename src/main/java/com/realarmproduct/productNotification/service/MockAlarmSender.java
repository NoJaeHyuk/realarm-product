package com.realarmproduct.productNotification.service;

import com.realarmproduct.common.enums.SupportType;
import org.springframework.stereotype.Component;

@Component
public class MockAlarmSender implements AlarmSender {

    private final SupportType supportType;

    public MockAlarmSender() {
        this.supportType = SupportType.EMAIL; // 기본 알림 타입을 EMAIL로 설정
    }

    @Override
    public boolean support(SupportType supportType) {
        return this.supportType == supportType;
    }

    @Override
    public void notify(Long userId, Long productId, Integer restockRound) {
        System.out.printf("Mock 알림 전송 [%s]: 유저 ID=%d, 상품 ID=%d, 재입고 회차=%d%n",
                supportType, userId, productId, restockRound);
    }
}
