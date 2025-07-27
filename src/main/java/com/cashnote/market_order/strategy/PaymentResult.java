package com.cashnote.market_order.strategy;

import lombok.*;


@Getter
@AllArgsConstructor
public class PaymentResult {
    private final boolean success;
    private final String code;
    private final String message;

    public static PaymentResult success(String transactionId) {
        return new PaymentResult(true, transactionId, "결제 성공");
    }

    public static PaymentResult fail(String errorCode) {
        return new PaymentResult(false, errorCode, "결제 실패: " + errorCode);
    }
}
