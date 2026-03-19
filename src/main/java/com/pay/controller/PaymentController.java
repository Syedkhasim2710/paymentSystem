package com.pay.controller;

import com.pay.model.PaymentStatus;
import com.pay.model.PaymentTransaction;
import com.pay.model.dto.PaymentRequest;
import com.pay.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    @Autowired
    private PaymentService service;

    @PostMapping("/initiate")
    public ResponseEntity<PaymentTransaction> initiate(@RequestBody PaymentRequest req) {
        return ResponseEntity.ok(service.initiate(req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentTransaction> getStatus(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping("/{txnId}/refund")
    @Operation(summary = "Initiate a Refund", description = "Calls the respective gateway to process a reversal")
    public ResponseEntity<PaymentTransaction> refund(
            @PathVariable String txnId,
            @RequestParam BigDecimal amount) {
        return ResponseEntity.ok(service.processRefund(txnId, amount));
    }

    // 2. UPDATE STATUS API
    @PatchMapping("/{txnId}/status")
    @Operation(summary = "Internal Status Update", description = "Updates the transaction state manually")
    public ResponseEntity<PaymentTransaction> updateStatus(
            @PathVariable String txnId,
            @RequestParam PaymentStatus status) {
        return ResponseEntity.ok(service.updateStatus(txnId, status));
    }
}