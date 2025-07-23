package com.cashnote.market_order.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_item")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "order_id", nullable = false)
	private Long orderId;

	@Column(name = "product_id", nullable = false)
	private Long productId;
	
	@Column(name = "quantity", nullable = false)
	private Integer quantity; // 수량
	
	@Column(name = "unit_price", nullable = false)
	private BigDecimal unitPrice; // 주문가
}
