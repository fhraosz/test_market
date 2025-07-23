package com.cashnote.market_order.domain.repository;

import com.cashnote.market_order.domain.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {}
