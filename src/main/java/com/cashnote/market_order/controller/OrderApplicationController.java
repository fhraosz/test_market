package com.cashnote.market_order.controller;

import com.cashnote.market_order.application.OrderApplicationService;
import com.cashnote.market_order.dto.request.OrderRequest;
import com.cashnote.market_order.dto.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.cashnote.market_order.util.ResponseUtils.created;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderApplicationController {
	private final OrderApplicationService orderApplicationService;

	@PostMapping("/create")
	public ResponseEntity<OrderResponse> create(@RequestBody OrderRequest request) {
		return created(orderApplicationService.createOrder(request));
	}
}
