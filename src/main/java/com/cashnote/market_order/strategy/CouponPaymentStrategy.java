package com.cashnote.market_order.strategy;

import com.cashnote.market_order.dto.PaymentDto;
import com.cashnote.market_order.domain.enums.PaymentType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 쿠폰 결제 전략 구현체 (Mock)
 */
@Component
public class CouponPaymentStrategy implements PaymentStrategy {

    @Override
    public PaymentType getType() {
        return PaymentType.COUPON;
    }

    @Override
    public PaymentResult pay(BigDecimal totalAmount, PaymentDto paymentDto) {
        // 쿠폰은 단독 주문 불가능, 서브 결제만 허용
        if (paymentDto.getIsMain()) {
            return PaymentResult.fail("쿠폰은 서브 결제만 가능합니다.");
        }
        // Mock 쿠폰 API 호출 성공 시나리오
        String couponTxId = "COUPON_TXN_" + System.currentTimeMillis();
        return PaymentResult.success(couponTxId);
    }
}
