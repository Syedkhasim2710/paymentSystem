package com.pay.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/payments/webhook")
public class WebhookController {
    @PostMapping("/callback")
    public ResponseEntity<String> handleCallback(@RequestBody Map<String, Object> payload) {
        // Logic to update status based on merchant_tid in payload
        return ResponseEntity.ok("Handled Securely");
    }
}