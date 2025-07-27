package com.cashnote.market_order.service;

import com.cashnote.market_order.domain.entity.OrderItemEntity;
import com.cashnote.market_order.domain.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderItemService  {
	private final OrderItemRepository orderItemRepository;

	public OrderItemEntity save(OrderItemEntity item) {
		return orderItemRepository.save(item);
	}
}
