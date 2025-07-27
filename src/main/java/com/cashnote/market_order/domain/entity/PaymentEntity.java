package com.cashnote.market_order.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.cashnote.market_order.domain.enums.PaymentStatus;

import com.cashnote.market_order.dto.PaymentDto;
import com.cashnote.market_order.strategy.PaymentResult;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "PAYMENT")
public class PaymentEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "order_id", nullable = false)
	private Long orderId; // 주문 ID

	@Column(name = "payment_type", nullable = false)
	private String paymentType; // 결제 타입

	@Column(name = "amount", nullable = false)
	private BigDecimal amount; // 결제 금액

	@Column(name = "is_main", nullable = false)
	private Boolean isMain; // 메인 결제 여부

	@Column(name = "payment_status", nullable = false)
	private String paymentStatus; // 결제 상태

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt; // 생성일

	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt; // 수정일

	@Column(name = "pg_transaction_id")
	private String pgTransactionId; // PG 거래 ID

	@Column(name = "points_used")
	private Integer pointsUsed; // 사용된 포인트

	@Column(name = "coupon_code")
	private String couponCode; // 쿠폰 코드

	@Column(name = "discount_coupon_amount")
	private BigDecimal discountCouponAmount; // 쿠폰 할인 금액

	@Column(name = "bnpl_transaction_id")
	private String bnplTransactionId; // 외상거래 ID

	public static PaymentEntity from(Long orderId, PaymentDto paymentDto, PaymentResult result) {
		LocalDateTime now = LocalDateTime.now();

		PaymentEntityBuilder builder = PaymentEntity.builder()
				.orderId(orderId)
				.paymentType(paymentDto.getType().name())
				.amount(paymentDto.getAmount())
				.isMain(paymentDto.getIsMain())
				.paymentStatus(result.isSuccess() ? PaymentStatus.SUCCESS.name() : PaymentStatus.FAILED.name())
				.createdAt(now)
				.updatedAt(now);

		switch (paymentDto.getType()) {
			case PG:
				builder.pgTransactionId(result.getCode());
				break;
			case POINT:
				builder.pointsUsed(paymentDto.getAmount().intValue());
				break;
			case COUPON:
				builder.couponCode(result.getCode());
				builder.discountCouponAmount(paymentDto.getAmount());
				break;
			case BNPL:
				builder.bnplTransactionId(result.getCode());
				break;
		}

		return builder.build();
	}
}




