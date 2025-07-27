package com.cashnote.market_order.dto;

import lombok.*;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDto {
	private Long productId;
    private Integer quantity;
}
