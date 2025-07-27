package com.cashnote.market_order.dto;

import com.cashnote.market_order.domain.enums.PaymentType;
import lombok.*;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDto {

	private PaymentType type;
	private BigDecimal amount;
	private Boolean isMain;
}
