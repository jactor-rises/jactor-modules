package com.github.jactor.persistence.builder;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public class SuppressValidInstanceExtension implements BeforeAllCallback, BeforeEachCallback, AfterEachCallback, FieldValidation.FieldValidator {
    private final Set<Class> suppressValidations;

    @SuppressWarnings("unused") public SuppressValidInstanceExtension() { // used by junit
        this(Collections.emptySet());
    }

    private SuppressValidInstanceExtension(Set<Class> suppressValidations) {
        this.suppressValidations = suppressValidations;
    }

    @Override public void beforeAll(ExtensionContext extensionContext) {
        FieldValidation.applyFieldValidator(this);
    }

    @Override public void beforeEach(ExtensionContext context) {
        FieldValidation.applyFieldValidator(this);
    }

    @Override public void afterEach(ExtensionContext context) {
        FieldValidation.useStandardValidation();
    }

    @Override public <E> Optional<MissingFields> validate(ValidInstance<E> validInstance, E bean) {
        if (suppressValidations.contains(bean.getClass()) || suppressValidations.isEmpty()) {
            return Optional.empty();
        }

        return validInstance.validate(bean, new MissingFields());
    }
}
