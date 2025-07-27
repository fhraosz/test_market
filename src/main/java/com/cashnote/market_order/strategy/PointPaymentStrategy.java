package com.cashnote.market_order.strategy;

import com.cashnote.market_order.dto.PaymentDto;
import com.cashnote.market_order.domain.enums.PaymentType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 캐시노트 포인트 결제 전략 구현체 (Mock)
 */
@Component
public class PointPaymentStrategy implements PaymentStrategy {

    @Override
    public PaymentType getType() {
        return PaymentType.POINT;
    }

    @Override
    public PaymentResult pay(BigDecimal totalAmount, PaymentDto paymentDto) {
        // 포인트는 서브 결제만 허용
        if (paymentDto.getIsMain()) {
            return PaymentResult.fail("포인트는 서브 결제만 가능합니다.");
        }
        // Mock 포인트 API 호출 성공 시나리오
        String pointTxId = "POINT_TXN_" + System.currentTimeMillis();
        return PaymentResult.success(pointTxId);
    }
}
