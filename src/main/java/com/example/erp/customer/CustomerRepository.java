package com.example.erp.customer;

import java.util.List;
import java.util.Optional;

/** "Data Access" layer for Customer — interface + one implementation. */
public interface CustomerRepository {
    void save(Customer customer);
    Optional<Customer> findByCode(String code);
    List<Customer> findAll();
    void delete(String code);
}
