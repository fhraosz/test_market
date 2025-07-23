package com.cashnote.market_order.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.cashnote.market_order.domain.enums.OrderStatus;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "order")
public class OrderEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "member_id", nullable = false)
	private Long memberId; // 주문자 ID

	@Column(name = "total_amount", nullable = false)
	private BigDecimal totalAmount; // 전체금액
	
	@Column(name = "create_at", nullable = false)
	private LocalDateTime createdAt;

	private String status;
}
