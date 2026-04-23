package com.example.erp.common;

/**
 * Thrown by service {@code beforeSave} / {@code beforeDeactivate} hooks when
 * a business rule is violated. The equivalent on ekon Platform would be
 * {@code OTException} raised from an {@code FMDefaultEvents} subclass.
 */
public class RuleException extends RuntimeException {
    public RuleException(String message) {
        super(message);
    }
}
