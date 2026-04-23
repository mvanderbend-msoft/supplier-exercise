package com.example.erp.article;

import java.math.BigDecimal;
import java.util.Objects;

public class Article {

    private final String code;
    private String description;
    private BigDecimal unitPrice;
    private boolean active = true;

    public Article(String code, String description, BigDecimal unitPrice) {
        this.code = Objects.requireNonNull(code);
        this.description = description;
        this.unitPrice = unitPrice;
    }

    public String getCode()         { return code; }
    public String getDescription()  { return description; }
    public BigDecimal getUnitPrice(){ return unitPrice; }
    public boolean isActive()       { return active; }

    public void setDescription(String description) { this.description = description; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public void deactivate() { this.active = false; }

    @Override public boolean equals(Object o) { return o instanceof Article a && code.equals(a.code); }
    @Override public int hashCode() { return code.hashCode(); }
}
