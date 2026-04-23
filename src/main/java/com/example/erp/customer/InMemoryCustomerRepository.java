package com.example.erp.customer;

import java.util.*;

public class InMemoryCustomerRepository implements CustomerRepository {

    private final Map<String, Customer> byCode = new LinkedHashMap<>();

    @Override public void save(Customer customer) {
        byCode.put(customer.getCode(), customer);
    }

    @Override public Optional<Customer> findByCode(String code) {
        return Optional.ofNullable(byCode.get(code));
    }

    @Override public List<Customer> findAll() {
        return new ArrayList<>(byCode.values());
    }

    @Override public void delete(String code) {
        byCode.remove(code);
    }
}
