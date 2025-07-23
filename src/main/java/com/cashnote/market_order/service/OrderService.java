package com.cashnote.market_order.service;

import java.lang.reflect.Member;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.cashnote.market_order.domain.entity.MemberEntity;
import com.cashnote.market_order.domain.enums.OrderStatus;
import com.cashnote.market_order.domain.enums.PaymentStatus;
import com.cashnote.market_order.domain.repository.MemberRepository;
import com.cashnote.market_order.domain.repository.OrderItemRepository;
import com.cashnote.market_order.domain.repository.OrderRepository;
import com.cashnote.market_order.domain.repository.PaymentRepository;
import com.cashnote.market_order.domain.repository.ProductRepository;
import com.cashnote.market_order.service.dto.OrderRequest;
import com.cashnote.market_order.service.dto.OrderResponse;
import com.mysql.cj.x.protobuf.MysqlxCrud.Order;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
	private final MemberRepository memberRepository;
	private final ProductRepository productRepository;
	private final OrderRepository orderRepository;
	private final OrderItemRepository orderItemRepository;
	private final PaymentRepository paymentRepository;

	@Transactional
	public OrderResponse createOrder(OrderRequest req) {
		// 회원 검증
		MemberEntity member = memberRepository.findById(req.).orElseThrow();

		// 총 금액 계산
		BigDecimal total = BigDecimal.ZERO;
		for (OrderRequest.OrderItemDTO it : req.getItems()) {
			Product p = productRepository.findById(it.getProductId()).orElseThrow();
			total = total.add(p.getPrice().multiply(BigDecimal.valueOf(it.getQuantity())));
		}

		// 결제 합계 검증
		BigDecimal paidSum = req.getPayments().stream().map(p -> p.getAmount()).reduce(BigDecimal.ZERO,
				BigDecimal::add);
		if (paidSum.compareTo(total) != 0) {
			throw new IllegalArgumentException("결제 금액 합계가 주문 총액과 일치하지 않습니다.");
		}

		// 주문 저장
		Order order = Order.builder().memberId(member.getId()).totalAmount(total).createdAt(LocalDateTime.now())
				.status(OrderStatus.CREATED).build();
		order = orderRepository.save(order);

		// 주문 항목 저장
		for (OrderRequest.OrderItemDTO it : req.getItems()) {
			Product p = productRepository.getReferenceById(it.getProductId());
			orderItemRepository.save(OrderItem.builder().orderId(order.getId()).productId(p.getId()).quantity(it.getQuantity())
					.unitPrice(p.getPrice()).build());
		}

		// 결제 정보 저장
		req.getPayments().forEach(pay -> {
			paymentRepo.save(Payment.builder().orderId(order.getId()).type(pay.getType()).amount(pay.getAmount())
					.isMain(pay.getIsMain()).status(PaymentStatus.SUCCESS).createdAt(LocalDateTime.now()).build());
		});

		// 주문 완료 상태 업데이트
		order.setStatus(OrderStatus.PAID);
		orderRepo.save(order);

		return OrderResponse.builder().orderId(order.getId()).status(order.getStatus().name())
				.createdAt(order.getCreatedAt()).build();
	}

}
