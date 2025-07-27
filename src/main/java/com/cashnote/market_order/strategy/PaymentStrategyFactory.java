package com.cashnote.market_order.strategy;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.cashnote.market_order.exception.OrderException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import com.cashnote.market_order.domain.enums.PaymentType;

/**
 * 결제 전략 제공 팩토리
 */
@Component
@Slf4j
public class PaymentStrategyFactory {
    private final Map<PaymentType, PaymentStrategy> strategies;

    public PaymentStrategyFactory(List<PaymentStrategy> list) {
        this.strategies = list.stream()
                .collect(Collectors.toUnmodifiableMap(PaymentStrategy::getType, s -> s));
    }

    public PaymentStrategy getStrategy(PaymentType type) {
        PaymentStrategy paymentStrategy = strategies.get(type);
        if (paymentStrategy == null) {
            log.error("지원하지 않는 결제수단 요청: {}", type);
            throw new OrderException("지원하지 않는 결제 수단입니다.");
        }
        return paymentStrategy;
    }
}
