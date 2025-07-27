package com.cashnote.market_order.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentType {
    PG("PG 결제 (신용카드, 계좌이체 등)"),
    POINT("캐시노트 포인트 결제"),
    COUPON("쿠폰 할인 결제"),
    BNPL("외상 결제 (BNPL)");

    private final String description;
}
