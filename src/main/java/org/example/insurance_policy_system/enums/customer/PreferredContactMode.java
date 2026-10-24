package org.example.insurance_policy_system.enums.customer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PreferredContactMode {
    Mobile,
    Email,
    SMS;

    @JsonCreator
    public static PreferredContactMode fromValue(String value) {
        if (value == null) {
            return null;
        }
        for (PreferredContactMode mode : values()) {
            if (mode.name().equalsIgnoreCase(value.trim())) {
                return mode;
            }
        }
        throw new IllegalArgumentException("Unsupported preferred contact mode: " + value);
    }

    @JsonValue
    public String toValue() {
        return name();
    }
}
