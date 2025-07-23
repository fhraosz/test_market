package com.cashnote.market_order.service.dto;

import lombok.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class OrderRequest {
    /** 주문할 회원의 ID */
    private Long memberId;

    /** 주문할 항목 목록 */
    private List<OrderItemDto> items;

    /** 결제 수단 목록 */
    private List<PaymentDto> payments;

}
