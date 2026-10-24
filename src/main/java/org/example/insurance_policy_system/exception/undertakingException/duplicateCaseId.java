package org.example.insurance_policy_system.exception.undertakingException;

public class duplicateCaseId extends RuntimeException {
    public duplicateCaseId(String message) {
        super(message);
    }
}
