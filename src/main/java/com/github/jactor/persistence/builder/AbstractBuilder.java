package com.gitlab.jactor.persistence.builder;

/**
 * A builder which validates fields on a bean before returning the instance validated with a {@link ValidInstance}
 *
 * @param <T> type of bean to build
 */
public abstract class AbstractBuilder<T> {
    private final ValidInstance<T> validInstance;

    protected AbstractBuilder(ValidInstance<T> validInstance) {
        this.validInstance = validInstance;
    }

    protected abstract T buildBean();

    public T build() {
        T bean = buildBean();

        FieldValidation.perform(validInstance, bean)
                .ifPresent(MissingFields::failWhenWhenFieldsAreMissing);

        return bean;
    }
}
