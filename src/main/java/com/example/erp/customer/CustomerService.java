package com.example.erp.customer;

import com.example.erp.common.RuleException;
import com.example.erp.customer.rules.VatValidator;
import com.example.erp.order.OrderRepository;
import com.example.erp.order.OrderStatus;

/**
 * Customer "customisation class" equivalent.
 *
 * <p>Exposes the public operations {@link #save(Customer)} and
 * {@link #deactivate(String)}, and keeps the rule logic in {@link #beforeSave}
 * / {@link #beforeDeactivate} so it mirrors the ekon FMDefaultEvents pattern
 * and stays easy to unit-test.</p>
 */
public class CustomerService {

    private final CustomerRepository customers;
    private final OrderRepository orders;
    private final VatValidator vatValidator;

    public CustomerService(CustomerRepository customers,
                           OrderRepository orders,
                           VatValidator vatValidator) {
        this.customers = customers;
        this.orders = orders;
        this.vatValidator = vatValidator;
    }

    public void save(Customer customer) {
        beforeSave(customer);
        customers.save(customer);
    }

    public java.util.List<Customer> findAll() {
        return customers.findAll();
    }

    public java.util.Optional<Customer> findByCode(String code) {
        return customers.findByCode(code);
    }

    public void writeActiveReport(java.io.PrintWriter out) {
        new CustomerListReport(customers).writeActiveCsv(out);
    }

    public void deactivate(String code) {
        var customer = customers.findByCode(code)
                .orElseThrow(() -> new RuleException("Customer not found: " + code));
        if (!customer.isActive()) return;
        beforeDeactivate(customer);
        customer.deactivate();
        customers.save(customer);
    }

    // ---- hook methods (mirrors FMDefaultEvents) ----------------------------

    protected void beforeSave(Customer c) {
        if (c.getLegalName() == null || c.getLegalName().isBlank()) {
            throw new RuleException("Legal name is required.");
        }
        if (!vatValidator.isValid(c.getCountry(), c.getVat())) {
            throw new RuleException("VAT number '" + c.getVat()
                    + "' is not valid for country " + c.getCountry() + ".");
        }
        if (c.getPaymentTermDays() < 0) {
            throw new RuleException("Payment term must be zero or positive.");
        }
    }

    protected void beforeDeactivate(Customer c) {
        var hasOpen = orders.findByCustomerCode(c.getCode()).stream()
                .anyMatch(o -> o.status() != OrderStatus.CLOSED);
        if (hasOpen) {
            throw new RuleException("Cannot deactivate customer " + c.getCode()
                    + ": it still has open orders.");
        }
    }
}
