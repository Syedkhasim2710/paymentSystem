package com.pay.strategy;

import com.pay.model.PaymentStatus;
import org.springframework.stereotype.Component;
import com.pay.model.PaymentGateway;
import com.pay.model.PaymentTransaction;

import java.math.BigDecimal;

@Component
public class RazorpayStrategy implements PaymentGatewayStrategy {
    @Override
    public PaymentGateway getGatewayName() {
        return PaymentGateway.RAZORPAY;
    }

    @Override
    public void process(PaymentTransaction txn) {
        // Mock logic: Add Razorpay specific flags
        txn.setMetadataJson("{\"provider\":\"Razorpay\", \"env\":\"sandbox\"}");
        txn.setStatus(PaymentStatus.INITIATED);
    }

    @Override
    public void refund(PaymentTransaction txn, BigDecimal refundAmount) {
        txn.setMetadataJson("{\"refundProvider\":\"Razorpay\", \"refundEnv\":\"sandbox\"}");
        txn.setStatus(PaymentStatus.REFUNDED);
    }
}