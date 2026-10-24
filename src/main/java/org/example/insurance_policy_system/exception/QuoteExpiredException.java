package org.example.insurance_policy_system.exception;

public class QuoteExpiredException extends RuntimeException {
    public QuoteExpiredException(String message) {
        super(message);
    }
}
