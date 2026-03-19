package com.pay.model.dto;

import com.pay.model.PaymentGateway;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

/**
 * SOLID Principle: Single Responsibility.
 * This DTO is strictly for capturing the client's payment intent.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
    private String orderId;
    private BigDecimal amount;
    private String currency; // e.g., "INR"
    private PaymentGateway gateway; // RAZORPAY, STRIPE, etc.

    // Generic metadata (e.g., {"vpa": "user@upi"} or {"cardToken": "tok_123"})
    private Map<String, Object> metadata;
}