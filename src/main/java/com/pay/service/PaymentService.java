package com.pay.service;

import com.pay.model.PaymentStatus;
import com.pay.model.PaymentTransaction;
import com.pay.model.dto.PaymentRequest;
import com.pay.repo.PaymentRepository;
import com.pay.strategy.PaymentGatewayStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository repository;
    private final PaymentGatewayFactory gatewayFactory;

    @Transactional
    public PaymentTransaction initiate(PaymentRequest request) {
        PaymentTransaction txn = new PaymentTransaction();
        txn.setAmount(request.getAmount());
        txn.setOrderId(request.getOrderId());
        txn.setGateway(request.getGateway());

        gatewayFactory.getStrategy(request.getGateway()).process(txn);
        return repository.save(txn);
    }

    public PaymentTransaction getById(String id) {
        return repository.findByOrderId(id).orElseThrow();
    }

    @Transactional
    public PaymentTransaction processRefund(String txnId, BigDecimal amount) {
        PaymentTransaction txn = repository.findById(txnId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (txn.getStatus() != PaymentStatus.SUCCESS) {
            throw new RuntimeException("Only successful transactions can be refunded");
        }

        // Delegate to the correct gateway strategy
        PaymentGatewayStrategy strategy = gatewayFactory.getStrategy(txn.getGateway());
        strategy.refund(txn, amount);

        return repository.save(txn);
    }

    // --- UPDATE STATUS LOGIC ---
    @Transactional
    public PaymentTransaction updateStatus(String txnId, PaymentStatus newStatus) {
        PaymentTransaction txn = repository.findById(txnId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        txn.setStatus(newStatus);
        return repository.save(txn);
    }
}