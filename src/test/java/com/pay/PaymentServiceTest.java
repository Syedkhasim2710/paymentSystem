package com.pay;

import com.pay.model.PaymentGateway;
import com.pay.model.PaymentTransaction;
import com.pay.model.dto.PaymentRequest;
import com.pay.repo.PaymentRepository;
import com.pay.service.PaymentGatewayFactory;
import com.pay.service.PaymentService;
import com.pay.strategy.PaymentGatewayStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository repository;
    @Mock private PaymentGatewayFactory factory;
    @Mock private PaymentGatewayStrategy mockStrategy;

    @InjectMocks
    private PaymentService service;

    @Test
    void testInitiatePayment_CallsCorrectStrategy() {
        // Arrange
        PaymentRequest req = new PaymentRequest("ORD1", new BigDecimal("100"), "INR", PaymentGateway.RAZORPAY, null);
        when(factory.getStrategy(PaymentGateway.RAZORPAY)).thenReturn(mockStrategy);
        when(repository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        // Act
        PaymentTransaction result = service.initiate(req);

        // Assert
        verify(mockStrategy, times(1)).process(any());
        assertEquals("ORD1", result.getOrderId());
    }
}