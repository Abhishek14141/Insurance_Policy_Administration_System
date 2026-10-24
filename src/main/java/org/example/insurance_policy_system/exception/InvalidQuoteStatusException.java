package org.example.insurance_policy_system.exception;

public class InvalidQuoteStatusException extends RuntimeException {
    public InvalidQuoteStatusException(String message) {
        super(message);
    }
}
