package com.cashnote.market_order.util;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@UtilityClass
public final class ResponseUtils {

    public static <T> ResponseEntity<T> created(T body) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(body);
    }

    public static <T> ResponseEntity<T> ok(T body) {
        return ResponseEntity
                .ok(body);
    }

    public static ResponseEntity<Void> noContent() {
        return ResponseEntity
                .noContent()
                .build();
    }
}
