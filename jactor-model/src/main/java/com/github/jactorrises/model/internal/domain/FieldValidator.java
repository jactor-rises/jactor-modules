package com.github.jactorrises.model.internal.domain;

import java.util.List;
import java.util.Optional;

public class FieldValidator {
    public <E> void validate(E domain, List<ValidateField<E>> validateFields) {
        if (validateFields != null) {
            validateFields.forEach(validateField -> validateField(domain, validateField));
        }
    }

    private <E> void validateField(E domain, ValidateField<E> validateField) {
        Optional<String> validationMsg = validateField.validateFields(domain);

        if (validationMsg.isPresent()) {
            throw new IllegalArgumentException(validationMsg.get());
        }
    }

    @FunctionalInterface
    public interface ValidateField<T> {
        Optional<String> validateFields(T domain);
    }
}
