package com.epam.esm.validator;

import org.springframework.stereotype.Component;

@Component
public class OrderValidator {
    private static final long MAX_PRICE = 100000;
    private static final long MIN_PRICE = 0;

    public boolean validateOrder(long totalPrice) {
        return totalPrice < MAX_PRICE && totalPrice > MIN_PRICE;
    }
}
