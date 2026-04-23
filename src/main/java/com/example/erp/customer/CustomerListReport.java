package com.example.erp.customer;

import java.io.PrintWriter;
import java.util.Comparator;

/**
 * "Active customers" report — the Report / RE equivalent.
 * Writes a CSV sorted by country, then by legal name.
 */
public class CustomerListReport {

    private final CustomerRepository customers;

    public CustomerListReport(CustomerRepository customers) {
        this.customers = customers;
    }

    public void writeActiveCsv(PrintWriter out) {
        out.println("code;legal_name;country;vat;payment_term_days");
        customers.findAll().stream()
                .filter(Customer::isActive)
                .sorted(Comparator.comparing(Customer::getCountry)
                        .thenComparing(Customer::getLegalName))
                .forEach(c -> out.printf("%s;%s;%s;%s;%d%n",
                        c.getCode(), c.getLegalName(), c.getCountry(),
                        c.getVat(), c.getPaymentTermDays()));
        out.flush();
    }
}
