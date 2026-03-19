package com.pay.strategy;

import com.pay.model.PaymentGateway;
import com.pay.model.PaymentStatus;
import com.pay.model.PaymentTransaction;

import java.math.BigDecimal;

public class PaypalGatewayStrategy implements PaymentGatewayStrategy {
    @Override
    public PaymentGateway getGatewayName() {
        return PaymentGateway.PAYPAL;
    }

    @Override
    public void process(PaymentTransaction txn) {
        // Mock logic: Add Razorpay specific flags
        txn.setMetadataJson("{\"provider\":\"Paypal\", \"env\":\"sandbox\"}");
        txn.setStatus(PaymentStatus.INITIATED);
    }

    @Override
    public void refund(PaymentTransaction txn, BigDecimal refundAmount) {
        // Mock logic: Add Razorpay specific flags
        txn.setMetadataJson("{\"refundProvider\":\"Paypal\", \"refundEnv\":\"sandbox\"}");
        txn.setStatus(PaymentStatus.REFUNDED);
    }
}
