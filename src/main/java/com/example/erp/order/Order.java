package com.example.erp.order;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Read-only order model used for business-rule checks in other services
 * (e.g. "a customer with open orders cannot be deactivated").
 */
public record Order(String code, String customerCode, LocalDate date, OrderStatus status) {
    public Order {
        Objects.requireNonNull(code);
        Objects.requireNonNull(customerCode);
        Objects.requireNonNull(date);
        Objects.requireNonNull(status);
    }
}
