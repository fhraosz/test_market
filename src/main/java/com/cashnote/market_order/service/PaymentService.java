package com.cashnote.market_order.service;

import com.cashnote.market_order.domain.repository.ProductRepository;
import com.cashnote.market_order.dto.OrderItemDto;
import com.cashnote.market_order.exception.OrderException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cashnote.market_order.domain.entity.PaymentEntity;
import com.cashnote.market_order.domain.repository.PaymentRepository;
import com.cashnote.market_order.dto.PaymentDto;
import com.cashnote.market_order.strategy.PaymentResult;
import com.cashnote.market_order.strategy.PaymentStrategy;
import com.cashnote.market_order.strategy.PaymentStrategyFactory;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
	private final PaymentRepository paymentRepository;
	private final PaymentStrategyFactory strategyFactory;
	private final ProductRepository productRepository;

	@Transactional
	public PaymentResult paymentProcess(Long orderId, BigDecimal totalAmount, PaymentDto paymentDto) {

		PaymentStrategy strategy = strategyFactory.getStrategy(paymentDto.getType());
		PaymentResult result = strategy.pay(totalAmount, paymentDto);

		PaymentEntity payment = PaymentEntity.from(orderId, paymentDto, result);
		paymentRepository.save(payment);

		if (!result.isSuccess()) {
			throw new OrderException("결제 처리에 실패했습니다.");
		}

		return result;
	}

	private void calculateTotalAmount(List<OrderItemDto> orderItemDtoList, BigDecimal totalAmount) {
		BigDecimal calculateTotalAmount = BigDecimal.ZERO;
		for (OrderItemDto it : orderItemDtoList) {
			BigDecimal price = productRepository.findById(it.getProductId())
					.orElseThrow(() -> {
						log.error("존재하지 않는 상품: id={}", it.getProductId());
						return new OrderException("상품 정보를 찾을 수 없습니다.");
					})
					.getPrice();
			calculateTotalAmount = calculateTotalAmount.add(price.multiply(BigDecimal.valueOf(it.getQuantity())));
		}

		if (calculateTotalAmount.compareTo(totalAmount) != 0) {
			log.error("요청 금액 불일치: calculateTotalAmount={}, totalAmount={}", calculateTotalAmount, totalAmount);
			throw new OrderException("유효하지 않은 주문 금액입니다.");
		}
	}
}
