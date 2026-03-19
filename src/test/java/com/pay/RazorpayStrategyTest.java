package com.pay;

import com.pay.model.PaymentTransaction;
import com.pay.model.PaymentStatus;
import com.pay.strategy.RazorpayStrategy;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RazorpayStrategyTest {

    private final RazorpayStrategy strategy = new RazorpayStrategy();

    @Test
    void testProcess_SetsMetadataAndStatus() {
        PaymentTransaction txn = new PaymentTransaction();

        strategy.process(txn);

        assertEquals(PaymentStatus.INITIATED, txn.getStatus());
        assertTrue(txn.getMetadataJson().contains("Razorpay"));
    }
}