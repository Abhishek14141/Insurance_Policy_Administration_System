package org.example.insurance_policy_system.exception;

public class DuplicateCaseException extends RuntimeException {
    public DuplicateCaseException(String message) {
        super(message);
    }
}
