package com.cashnote.market_order.dto.response;

import com.cashnote.market_order.domain.entity.OrderEntity;
import com.cashnote.market_order.strategy.PaymentResult;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
	private Long orderId;
	private String status;
	private LocalDateTime createdAt;
	private List<PaymentResult> payments;

	public static OrderResponse of(OrderEntity order, List<PaymentResult> results) {
		return OrderResponse.builder()
				.orderId(order.getId())
				.status(order.getStatus())
				.createdAt(order.getCreatedAt())
				.payments(results)
				.build();
	}

}
