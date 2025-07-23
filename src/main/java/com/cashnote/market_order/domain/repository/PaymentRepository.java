package com.cashnote.market_order.domain.repository;

import com.cashnote.market_order.domain.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {}
