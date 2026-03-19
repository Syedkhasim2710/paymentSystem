package com.pay.strategy;

import com.pay.model.PaymentGateway;
import com.pay.model.PaymentStatus;
import com.pay.model.PaymentTransaction;

import java.math.BigDecimal;

public class StripeGatewayStrategy implements PaymentGatewayStrategy {
    @Override
    public PaymentGateway getGatewayName() {
        return PaymentGateway.STRIPE;
    }

    @Override
    public void process(PaymentTransaction txn) {
        // Mock logic: Add Razorpay specific flags
        txn.setMetadataJson("{\"provider\":\"Stripe\", \"env\":\"sandbox\"}");
        txn.setStatus(PaymentStatus.INITIATED);
    }

    @Override
    public void refund(PaymentTransaction txn, BigDecimal refundAmount) {
        txn.setMetadataJson("{\"refundProvider\":\"Stripe\", \"refundEnv\":\"sandbox\"}");
        txn.setStatus(PaymentStatus.REFUNDED);
    }
}

