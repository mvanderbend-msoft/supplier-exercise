package com.example.erp.customer.rules;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * Very small, offline VAT-number format validator.
 *
 * <p>Checks only the structural format — not VIES reachability. Extend the
 * {@link #PATTERNS} map to support more countries.</p>
 */
public class VatValidator {

    private static final Map<String, Pattern> PATTERNS = Map.of(
            "ES", Pattern.compile("^ES[A-Z0-9][0-9]{7}[A-Z0-9]$"),
            "FR", Pattern.compile("^FR[0-9A-Z]{2}[0-9]{9}$"),
            "PT", Pattern.compile("^PT[0-9]{9}$"),
            "IT", Pattern.compile("^IT[0-9]{11}$"),
            "DE", Pattern.compile("^DE[0-9]{9}$")
    );

    /**
     * @return true if {@code vat} is a structurally valid VAT number for
     *              {@code country}, false otherwise (including unknown country).
     */
    public boolean isValid(String country, String vat) {
        if (country == null || vat == null) return false;
        var p = PATTERNS.get(country.toUpperCase());
        if (p == null) return false;
        return p.matcher(vat.toUpperCase().replace(" ", "")).matches();
    }
}
