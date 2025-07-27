package com.cashnote.market_order.application;

import com.cashnote.market_order.domain.entity.MemberEntity;
import com.cashnote.market_order.domain.entity.OrderEntity;
import com.cashnote.market_order.domain.entity.OrderItemEntity;
import com.cashnote.market_order.dto.*;
import com.cashnote.market_order.dto.request.OrderRequest;
import com.cashnote.market_order.dto.response.OrderResponse;
import com.cashnote.market_order.exception.OrderException;
import com.cashnote.market_order.service.*;
import com.cashnote.market_order.strategy.PaymentResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cashnote.market_order.domain.enums.OrderStatus.CREATED;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderApplicationService {

	private final OrderService orderService;
	private final OrderItemService orderItemService;
	private final PaymentService paymentService;
	private final MemberService memberService;
	private final ProductService productService;

	@Transactional
	public OrderResponse createOrder(OrderRequest request) {
		// 1. 회원 검증
		MemberEntity memberEntity = memberService.findMemberEntityById(request.getMemberId());

		// 2. 상품별 가격 맵 생성
		Map<Long, BigDecimal> priceMap = buildPriceMap(request.getOrderItemDtoList());

		// 3. 총 금액 계산
		calculateTotalAmount(priceMap, request.getOrderItemDtoList(), request.getTotalAmount());

		// 4. 주문 생성 및 저장
		OrderEntity orderEntity = createAndSaveOrder(memberEntity.getId(), request.getTotalAmount());
		log.debug("주문 저장 완료: orderId={}", orderEntity.getId());

		// 5. 주문 항목 저장
		saveOrderItems(orderEntity.getId(), request.getOrderItemDtoList(), priceMap);
		log.debug("주문 항목 저장 완료: orderId={}, itemCount={}", orderEntity.getId(), request.getOrderItemDtoList().size());

		// 6. 결제 처리
		List<PaymentResult> results = processPayments(orderEntity, request);
		log.debug("결제 처리 결과: {}", results);

		// 7. 주문 최종 상태 처리
		finalizeOrder(orderEntity, results);
		log.debug("주문 상태 업데이트: orderId={}, status={}", orderEntity.getId(), orderEntity.getStatus());

		// 8. 응답 DTO 생성 및 반환
		OrderResponse response = buildResponse(orderEntity, results);
		log.info("주문 생성 완료: orderId={}, status={}", response.getOrderId(), response.getStatus());
		return response;
	}

	private Map<Long, BigDecimal> buildPriceMap(List<OrderItemDto> items) {
		Map<Long, BigDecimal> priceMap = new HashMap<>();
		for (OrderItemDto it : items) {
			BigDecimal price = productService.getPriceById(it.getProductId())
					.orElseThrow(() -> {
						log.error("상품 정보 없음: id={}", it.getProductId());
						return new OrderException("상품 정보를 찾을 수 없습니다.");
					});
			priceMap.put(it.getProductId(), price);
		}
		return priceMap;
	}

	/**
	 * 주문 아이템 목록으로 총 금액 계산
	 */
	private void calculateTotalAmount(Map<Long, BigDecimal> priceMap, List<OrderItemDto> orderItemDtoList, BigDecimal totalAmount) {

		BigDecimal actualTotal = BigDecimal.ZERO;
		List<String> detailLogs = new ArrayList<>();

		for (OrderItemDto it : orderItemDtoList) {
			BigDecimal productPrice = priceMap.get(it.getProductId());
			BigDecimal lineTotal = productPrice.multiply(BigDecimal.valueOf(it.getQuantity()));
			actualTotal = actualTotal.add(lineTotal);

			detailLogs.add(String.format("상품Id=%d, 단가=%s, 수량=%d, 합산금액=%s",it.getProductId(), productPrice, it.getQuantity(), lineTotal));
		}

		if (actualTotal.compareTo(totalAmount) != 0) {
			log.error("요청 금액 불일치: 실제합계={} vs 청구합계={} | 상세내역={}",actualTotal, totalAmount, detailLogs);
			throw new OrderException("유효하지 않은 주문 금액입니다.");
		}
	}

	/**
	 * 새 주문 엔티티 생성 및 저장
	 */
	private OrderEntity createAndSaveOrder(Long memberId, BigDecimal tatalAmount) {
		OrderEntity order = OrderEntity.of(memberId, tatalAmount, CREATED.name());
		return orderService.save(order);
	}

	/**
	 * 주문 항목을 저장
	 */
	private void saveOrderItems(Long orderId, List<OrderItemDto> items, Map<Long, BigDecimal> priceMap) {
		for (OrderItemDto orderItemDto : items) {
			BigDecimal price = priceMap.get(orderItemDto.getProductId());
			OrderItemEntity item = OrderItemEntity.of(orderId, orderItemDto.getProductId(), orderItemDto.getQuantity(), price);
			orderItemService.save(item);
		}
	}

	/**
	 * 결제 전략에 따라 결제 처리 및 결과 저장
	 */
	private List<PaymentResult> processPayments(OrderEntity orderEntity, OrderRequest request) {
		List<PaymentResult> results = new ArrayList<>();
		for (PaymentDto paymentDto : request.getPaymentDtoList()) {
			PaymentResult paymentResult = paymentService.paymentProcess(orderEntity.getId(), request.getTotalAmount(), paymentDto);
			results.add(paymentResult);
		}
		return results;
	}

	/**
	 * 모든 결제 성공 시 주문 완료 처리
	 */
	private void finalizeOrder(OrderEntity order, List<PaymentResult> results) {
		if (results.stream().allMatch(PaymentResult::isSuccess)) {
			order.pay();
			orderService.save(order);
			log.info("주문 결제 완료: orderId={}", order.getId());
		}
	}

	private OrderResponse buildResponse(OrderEntity orderEntity, List<PaymentResult> paymentResultList) {
		return OrderResponse.of(orderEntity, paymentResultList);
	}
}
