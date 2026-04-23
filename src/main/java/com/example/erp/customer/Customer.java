package com.example.erp.customer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Customer entity — the header record of the Customer aggregate.
 *
 * <p>Mutable because maintenance screens edit it in place. Equality is based
 * on the business code.</p>
 */
public class Customer {

    private final String code;
    private String legalName;
    private String country;           // ISO-3166 alpha-2
    private String vat;
    private int paymentTermDays;
    private boolean active = true;
    private final List<CustomerContact> contacts = new ArrayList<>();

    public Customer(String code, String legalName, String country, String vat, int paymentTermDays) {
        this.code = Objects.requireNonNull(code);
        this.legalName = legalName;
        this.country = country;
        this.vat = vat;
        this.paymentTermDays = paymentTermDays;
    }

    public String getCode()            { return code; }
    public String getLegalName()       { return legalName; }
    public String getCountry()         { return country; }
    public String getVat()             { return vat; }
    public int    getPaymentTermDays() { return paymentTermDays; }
    public boolean isActive()          { return active; }
    public List<CustomerContact> getContacts() { return contacts; }

    public void setLegalName(String legalName)             { this.legalName = legalName; }
    public void setCountry(String country)                 { this.country = country; }
    public void setVat(String vat)                         { this.vat = vat; }
    public void setPaymentTermDays(int paymentTermDays)    { this.paymentTermDays = paymentTermDays; }

    public void deactivate() { this.active = false; }
    public void activate()   { this.active = true; }

    @Override public boolean equals(Object o) {
        return o instanceof Customer c && code.equals(c.code);
    }
    @Override public int hashCode() { return code.hashCode(); }
    @Override public String toString() {
        return "Customer[" + code + " " + legalName + " (" + country + ") active=" + active + "]";
    }
}
