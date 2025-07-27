package com.cashnote.market_order.strategy;

import com.cashnote.market_order.domain.enums.PaymentType;
import com.cashnote.market_order.dto.PaymentDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PgPaymentStrategy implements PaymentStrategy {

    @Override
    public PaymentType getType() {
        return PaymentType.PG;
    }

    @Override
    public PaymentResult pay(BigDecimal totalAmount, PaymentDto paymentDto) {
        // PG는 메인 결제만 허용
        if (!paymentDto.getIsMain()) {
            return PaymentResult.fail("PG는 메인 결제만 가능합니다.");
        }
        // Mock PG API 호출 성공 시나리오
        String transactionId = "PG_TXN_" + System.currentTimeMillis();
        return PaymentResult.success(transactionId);
    }
}
