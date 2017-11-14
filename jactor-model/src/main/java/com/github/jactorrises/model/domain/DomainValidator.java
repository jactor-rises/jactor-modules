package com.github.jactorrises.model.domain;

import java.util.ArrayList;
import java.util.List;

public abstract class DomainValidator<T> {
    private List<String> errors = new ArrayList<>();

    public abstract void validate(T domain);

    protected void addIfInvalid(boolean invalid, String fieldName, FieldValidation fieldValidation) {
        if (invalid) {
            errors.add("Field '{0}' {1}"
                    .replace("{0}", fieldName)
                    .replace("{1}", fieldValidation.reason())
            );
        }
    }

    public void runOn(T domain) {
        validate(domain);

        if (!errors.isEmpty()) {
            throw new IllegalStateException(
                    domain.getClass().getSimpleName() +
                            " is in an invalid state\n- " +
                            String.join("\n- ",  errors)
            );
        }
    }

    public enum FieldValidation {
        EMPTY("cannot be empty"), REQUIRED("cannot be null");

        private final String reason;

        FieldValidation(String reason) {
            this.reason = reason;
        }

        public String reason() {
            return reason;
        }
    }
}
