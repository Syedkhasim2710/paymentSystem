package com.pay;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PaymentControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testWebhook_Unauthorized_WithoutKey() throws Exception {
        mockMvc.perform(post("/api/payments/webhook/callback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"SUCCESS\"}"))
                .andExpect(status().isUnauthorized()); // 401
    }

    @Test
    void testWebhook_Authorized_WithCorrectKey() throws Exception {
        mockMvc.perform(post("/api/payments/webhook/callback")
                        .header("X-API-KEY", "FLIPKART_SECRET_2026") // The secret from properties
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"merchant_tid\":\"123\", \"status\":\"SUCCESS\"}"))
                .andExpect(status().isOk()); // 200
    }
}