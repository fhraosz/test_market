package com.cashnote.market_order.service;

import com.cashnote.market_order.domain.entity.OrderEntity;
import com.cashnote.market_order.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

	private final OrderRepository orderRepository;

	@Transactional
	public OrderEntity save(OrderEntity order) {
		return orderRepository.save(order);
	}
}
