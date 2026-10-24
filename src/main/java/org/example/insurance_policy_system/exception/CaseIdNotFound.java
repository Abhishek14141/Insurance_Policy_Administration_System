package org.example.insurance_policy_system.exception;

public class CaseIdNotFound extends RuntimeException {
    public CaseIdNotFound(String message) {
        super(message);
    }
}
