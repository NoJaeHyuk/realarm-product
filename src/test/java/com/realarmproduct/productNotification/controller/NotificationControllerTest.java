package com.realarmproduct.productNotification.controller;

import com.realarmproduct.productNotification.service.ReStockNotificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReStockNotificationService reStockNotificationService;

    @Test
    @DisplayName("유효한 request 파라미터 일 시 요청이 완료된다.")
    void reStock_Success() throws Exception {
        Long validProductId = 1L;

        doNothing().when(reStockNotificationService).sendReStockNotifications(validProductId);

        mockMvc.perform(post("/products/{productId}/notifications/re-stock", validProductId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("유효하지 않은 request 파라미터 일 시 400에러 발생한다.")
    void reStock_Failure_InvalidProductId() throws Exception {
        Long invalidProductId = 0L;

        mockMvc.perform(post("/products/{productId}/notifications/re-stock", invalidProductId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
