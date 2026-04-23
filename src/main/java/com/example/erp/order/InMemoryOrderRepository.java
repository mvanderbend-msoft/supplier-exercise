package com.example.erp.order;

import java.util.*;

public class InMemoryOrderRepository implements OrderRepository {

    private final Map<String, Order> byCode = new LinkedHashMap<>();

    @Override public void save(Order order) { byCode.put(order.code(), order); }

    @Override public List<Order> findAll() { return new ArrayList<>(byCode.values()); }

    @Override public List<Order> findByCustomerCode(String customerCode) {
        return byCode.values().stream()
                .filter(o -> o.customerCode().equals(customerCode))
                .toList();
    }
}
