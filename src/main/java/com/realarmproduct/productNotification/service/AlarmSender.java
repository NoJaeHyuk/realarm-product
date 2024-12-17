package com.realarmproduct.productNotification.service;

import com.realarmproduct.common.enums.SupportType;

public interface AlarmSender {

    /**
     * 지원하는 알림 타입인지 확인
     *
     * @param supportType 알림 타입
     * @return 지원 여부
     */
    boolean support(SupportType supportType);

    /**
     * 알림 전송 메서드
     *
     * @param userId       유저 ID
     * @param productId    상품 ID
     * @param restockRound 재입고 회차
     */
    void notify(Long userId, Long productId, Integer restockRound);
}
