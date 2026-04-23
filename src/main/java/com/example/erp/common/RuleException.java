package com.example.erp.common;

/**
 * Thrown by service {@code beforeSave} / {@code beforeDeactivate} hooks when
 * a business rule is violated.
 */
public class RuleException extends RuntimeException {
    public RuleException(String message) {
        super(message);
    }
}
