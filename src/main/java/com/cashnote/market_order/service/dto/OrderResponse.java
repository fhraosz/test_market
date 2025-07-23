package com.cashnote.market_order.service.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
	private Long orderId;
	private String status;
	private LocalDateTime createdAt;
}
