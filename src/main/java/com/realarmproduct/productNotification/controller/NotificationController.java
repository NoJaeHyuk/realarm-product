package com.realarmproduct.productNotification.controller;

import com.realarmproduct.productNotification.service.ReStockNotificationService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final ReStockNotificationService reStockNotificationService;

    @PostMapping("/products/{productId}/notifications/re-stock")
    public ResponseEntity<Void> reStock(@Min(1) @PathVariable Long productId) {
        return ResponseEntity.ok().build();
    }
}
