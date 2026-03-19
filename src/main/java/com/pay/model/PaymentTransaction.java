package com.pay.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Table(name = "payment_transaction")
@Entity
@Data
public class PaymentTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String orderId;

    @Column(updatable = false, nullable = false, unique = true)
    private String transactionId = java.util.UUID.randomUUID().toString();
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private PaymentGateway gateway;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(columnDefinition = "TEXT")
    private String metadataJson; // Generic storage for gateway response
}
