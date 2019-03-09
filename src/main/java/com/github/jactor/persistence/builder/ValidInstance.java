package com.gitlab.jactor.persistence.builder;

import java.util.Optional;

@FunctionalInterface
public interface ValidInstance<T> {
    /**
     * @param bean          to validate
     * @param missingFields is appended with fields that are missing
     * @return an {@link Optional} {@link MissingFields} containing errors to fields in a bean. When {@link Optional#isPresent()} it
     * still needs to contain fields with errors to be a {@link MissingFields}
     */
    Optional<MissingFields> validate(T bean, MissingFields missingFields);
}

