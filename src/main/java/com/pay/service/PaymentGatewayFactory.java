package com.pay.service;

import com.pay.model.PaymentGateway;
import com.pay.strategy.PaymentGatewayStrategy;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class PaymentGatewayFactory {

    private final Map<PaymentGateway, PaymentGatewayStrategy> strategies = new EnumMap<>(PaymentGateway.class);

    public PaymentGatewayFactory(List<PaymentGatewayStrategy> strategyList) {
        strategyList.forEach(s -> strategies.put(s.getGatewayName(), s));
    }

    public PaymentGatewayStrategy getStrategy(PaymentGateway gateway) {
        return Optional.ofNullable(strategies.get(gateway))
                .orElseThrow(() -> new RuntimeException("Gateway not supported"));
    }
}
