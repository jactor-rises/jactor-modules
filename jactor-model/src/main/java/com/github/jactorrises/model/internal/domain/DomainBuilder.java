package com.github.jactorrises.model.internal.domain.builder;

import java.util.List;

/**
 * A builder which does not return a bean instance before all fields are validated using
 * {@link FieldValidator.ValidateField}
 *
 * @param <T> type of domain to build
 */
public abstract class DomainBuilder<T> {
    private static FieldValidator fieldValidator;
    private final List<FieldValidator.ValidateField<T>> validateFields;

    protected DomainBuilder(List<FieldValidator.ValidateField<T>> validateFields) {
        this.validateFields = validateFields;
    }

    protected abstract T addRequiredFields();

    public T build() {
        T bean = addRequiredFields();
        fieldValidator.validate(bean, validateFields);

        return bean;
    }

    protected static void useFieldValidator(FieldValidator fieldValidator) {
        DomainBuilder.fieldValidator = fieldValidator;
    }

    static {
        useFieldValidator(new FieldValidator());
    }
}
