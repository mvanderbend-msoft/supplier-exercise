package com.example.erp.order;

import java.util.List;

public interface OrderRepository {
    void save(Order order);
    List<Order> findAll();
    List<Order> findByCustomerCode(String customerCode);
}
