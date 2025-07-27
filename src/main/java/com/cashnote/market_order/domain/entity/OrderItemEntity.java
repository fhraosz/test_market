package com.cashnote.market_order.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "ORDER_ITEM")
public class OrderItemEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "order_id", nullable = false)
	private Long orderId;

	@Column(name = "product_id", nullable = false)
	private Long productId;
	
	@Column(name = "quantity", nullable = false)
	private Integer quantity;
	
	@Column(name = "unit_price", nullable = false)
	private BigDecimal unitPrice;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	public static OrderItemEntity of(Long orderId, Long productId, Integer quantity, BigDecimal unitPrice) {
		LocalDateTime now = LocalDateTime.now();

		return OrderItemEntity.builder()
				.orderId(orderId)
				.productId(productId)
				.quantity(quantity)
				.unitPrice(unitPrice)
				.createdAt(now)
				.updatedAt(now)
				.build();
	}
}
