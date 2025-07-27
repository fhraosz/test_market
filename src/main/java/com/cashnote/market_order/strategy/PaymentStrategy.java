package com.cashnote.market_order.strategy;

import com.cashnote.market_order.dto.PaymentDto;
import com.cashnote.market_order.domain.enums.PaymentType;

import java.math.BigDecimal;

public interface PaymentStrategy {

    PaymentType getType();
    PaymentResult pay(BigDecimal totalAmount, PaymentDto paymentDto);
}
