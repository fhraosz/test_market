package com.cashnote.market_order.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 주문 처리 중 발생하는 예외
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OrderException extends RuntimeException {
    public OrderException(String message) {
        super(message);
    }
}
