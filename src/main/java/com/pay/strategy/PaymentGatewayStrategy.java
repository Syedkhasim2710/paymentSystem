package com.pay.strategy;

import com.pay.model.PaymentGateway;
import com.pay.model.PaymentTransaction;

import java.math.BigDecimal;

public interface PaymentGatewayStrategy {
    PaymentGateway getGatewayName();
    void process(PaymentTransaction txn);
    void refund(PaymentTransaction txn, BigDecimal refundAmount);
}