package com.gitlab.jactor.persistence.builder;

import java.util.Optional;

public final class FieldValidation {
    private static FieldValidation instance = new FieldValidation(new FieldValidatorForBuild()::validate);

    private final FieldValidator fieldValidator;

    private FieldValidation(FieldValidator fieldValidator) {
        this.fieldValidator = fieldValidator;
    }


    public static <T> Optional<MissingFields> performWithStandardValidation(ValidInstance<T> validInstance, T bean) {
        return new FieldValidatorForBuild().validate(validInstance, bean);
    }

    static <T> Optional<MissingFields> perform(ValidInstance<T> validInstance, T bean) {
        return instance.fieldValidator.validate(validInstance, bean);
    }

    public static void useStandardValidation() {
        instance = new FieldValidation(new FieldValidatorForBuild()::validate);
    }

    public static void applyFieldValidator(FieldValidator fieldValidator) {
        instance = new FieldValidation(fieldValidator);
    }

    private static class FieldValidatorForBuild {
        public <B> Optional<MissingFields> validate(ValidInstance<B> validInstance, B bean) {
            return validInstance.validate(bean, new MissingFields());
        }
    }

    @FunctionalInterface
    public interface FieldValidator {
        <E> Optional<MissingFields> validate(ValidInstance<E> validInstance, E bean);
    }
}
