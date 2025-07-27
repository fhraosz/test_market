package com.cashnote.market_order.strategy;

import com.cashnote.market_order.dto.PaymentDto;
import com.cashnote.market_order.domain.enums.PaymentType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * BNPL(외상결제) 전략 구현체 (Mock)
 */
@Component
public class BnplPaymentStrategy implements PaymentStrategy {

    @Override
    public PaymentType getType() {
        return PaymentType.BNPL;
    }

    @Override
    public PaymentResult pay(BigDecimal totalAmount, PaymentDto paymentDto) {
        // BNPL은 단독 메인 결제만 허용
        if (!paymentDto.getIsMain()) {
            return PaymentResult.fail("BNPL은 메인 결제만 가능합니다.");
        }
        // BNPL은 전체 금액을 커버해야 함
        if (paymentDto.getAmount().compareTo(totalAmount) != 0) {
            return PaymentResult.fail("BNPL은 전체 주문 금액으로만 결제할 수 있습니다.");
        }
        // Mock BNPL API 호출 성공 시나리오
        String loanId = "BNPL_LOAN_" + System.currentTimeMillis();
        return PaymentResult.success(loanId);
    }
}
