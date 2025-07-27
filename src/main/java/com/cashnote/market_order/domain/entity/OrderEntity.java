package com.cashnote.market_order.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.cashnote.market_order.domain.enums.OrderStatus.PAID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "`ORDER`")
public class OrderEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "member_id", nullable = false)
	private Long memberId; // 주문자 ID

	@Column(name = "total_amount", nullable = false)
	private BigDecimal totalAmount; // 전체금액
	
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	@Column(name = "status", nullable = false)
	private String status;

	public static OrderEntity of(Long memberId, BigDecimal totalAmount, String status) {
		LocalDateTime now = LocalDateTime.now();

		return OrderEntity.builder()
				.memberId(memberId)
				.totalAmount(totalAmount)
				.createdAt(now)
				.updatedAt(now)
				.status(status)
				.build();
	}

	public void pay() {
		this.status = PAID.name();
	}
}
