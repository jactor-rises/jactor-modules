package com.github.jactorrises.model.internal.domain.builder;

import java.util.List;
import java.util.Optional;

public class FieldValidator {
    public <E> void validate(E domain, List<ValidateField<E>> validateFields) {
        if (validateFields != null) {
            failWhenFieldIsInvalid(domain, validateFields);
        }
    }

    private <E> void failWhenFieldIsInvalid(E domain, List<ValidateField<E>> validateFields) {
        for (ValidateField<E> validateField : validateFields) {
            validateField(domain, validateField);
        }
    }

    private <E> void validateField(E domain, ValidateField<E> validateField) {
        Optional<String> requiredFieldMsg = validateField.validateFields(domain);

        if (requiredFieldMsg.isPresent()) {
            throw new IllegalArgumentException(requiredFieldMsg.get());
        }
    }

    @FunctionalInterface
    public interface ValidateField<T> {
        Optional<String> validateFields(T domain);
    }
}
