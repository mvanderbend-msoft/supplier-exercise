package com.example.erp.customer.rules;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VatValidatorTest {

    private final VatValidator v = new VatValidator();

    @Test void acceptsValidSpanishVat() {
        assertTrue(v.isValid("ES", "ESB12345674"));
    }

    @Test void acceptsValidFrenchVat() {
        assertTrue(v.isValid("FR", "FR40303265045"));
    }

    @Test void rejectsWrongCountryForFormat() {
        assertFalse(v.isValid("PT", "ESB12345674"));
    }

    @Test void rejectsUnknownCountry() {
        assertFalse(v.isValid("ZZ", "ZZ123"));
    }

    @Test void rejectsNulls() {
        assertFalse(v.isValid(null, "ESB12345674"));
        assertFalse(v.isValid("ES", null));
    }
}
