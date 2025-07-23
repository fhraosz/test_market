package com.cashnote.market_order.domain.repository;

import com.cashnote.market_order.domain.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {}
