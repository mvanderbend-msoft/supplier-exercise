package com.example.erp.customer;

import com.example.erp.common.RuleException;
import com.example.erp.customer.rules.VatValidator;
import com.example.erp.order.InMemoryOrderRepository;
import com.example.erp.order.Order;
import com.example.erp.order.OrderStatus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CustomerServiceTest {

    private InMemoryCustomerRepository customers;
    private InMemoryOrderRepository orders;
    private CustomerService service;

    @BeforeEach void setUp() {
        customers = new InMemoryCustomerRepository();
        orders = new InMemoryOrderRepository();
        service = new CustomerService(customers, orders, new VatValidator());
    }

    @Test void savesCustomerWhenValid() {
        var c = new Customer("C-1", "Acme", "ES", "ESB12345674", 30);
        service.save(c);
        assertTrue(customers.findByCode("C-1").isPresent());
    }

    @Test void rejectsInvalidVat() {
        var c = new Customer("C-1", "Acme", "ES", "NOT-A-VAT", 30);
        assertThrows(RuleException.class, () -> service.save(c));
    }

    @Test void rejectsBlankLegalName() {
        var c = new Customer("C-1", "  ", "ES", "ESB12345674", 30);
        assertThrows(RuleException.class, () -> service.save(c));
    }

    @Test void rejectsNegativePaymentTerm() {
        var c = new Customer("C-1", "Acme", "ES", "ESB12345674", -1);
        assertThrows(RuleException.class, () -> service.save(c));
    }

    @Test void deactivatesWhenNoOpenOrders() {
        var c = new Customer("C-1", "Acme", "ES", "ESB12345674", 30);
        service.save(c);
        orders.save(new Order("O-1", "C-1", LocalDate.now(), OrderStatus.CLOSED));
        service.deactivate("C-1");
        assertFalse(customers.findByCode("C-1").orElseThrow().isActive());
    }

    @Test void refusesDeactivateWhenOpenOrders() {
        var c = new Customer("C-1", "Acme", "ES", "ESB12345674", 30);
        service.save(c);
        orders.save(new Order("O-1", "C-1", LocalDate.now(), OrderStatus.OPEN));
        assertThrows(RuleException.class, () -> service.deactivate("C-1"));
    }
}
