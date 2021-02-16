package com.epam.esm.validator;

import org.springframework.stereotype.Component;

@Component
public class OrderValidator {
    private static final long MAX_PRICE = 100000;

    public boolean validateOrder(long totalPrice) {
        return validatePrice(totalPrice);
    }

    public boolean validatePrice(long price) {
        return price <= MAX_PRICE;
    }
}
