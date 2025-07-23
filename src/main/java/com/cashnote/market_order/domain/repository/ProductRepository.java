package com.cashnote.market_order.domain.repository;

import com.cashnote.market_order.domain.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}