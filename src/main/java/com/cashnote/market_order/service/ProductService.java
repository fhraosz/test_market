package com.cashnote.market_order.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cashnote.market_order.domain.entity.ProductEntity;
import com.cashnote.market_order.domain.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
	private final ProductRepository productRepository;

	public Optional<BigDecimal> getPriceById(Long productId) {
		return productRepository.findById(productId)
				.map(ProductEntity::getPrice);
	}
}
