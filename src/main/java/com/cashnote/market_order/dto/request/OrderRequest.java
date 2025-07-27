package com.cashnote.market_order.dto.request;

import com.cashnote.market_order.dto.OrderItemDto;
import com.cashnote.market_order.dto.PaymentDto;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class OrderRequest {

    private Long memberId;
    private BigDecimal totalAmount;
    private List<OrderItemDto> orderItemDtoList;
    private List<PaymentDto> paymentDtoList;
}
