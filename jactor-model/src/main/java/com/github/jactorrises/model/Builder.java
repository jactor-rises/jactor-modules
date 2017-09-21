package com.github.jactorrises.model;

import java.util.Collections;
import java.util.List;

/**
 * A builder which val validate all fields on a bean instance before returning the instance
 * {@link FieldValidator.ValidateField}
 *
 * @param <T> type of domain to build
 */
public abstract class Builder<T> {
    private static FieldValidator fieldValidator;
    private final List<FieldValidator.ValidateField<T>> validateFields;

    protected Builder() {
        this(Collections.emptyList());
    }

    protected Builder(List<FieldValidator.ValidateField<T>> validateFields) {
        this.validateFields = validateFields;
    }

    protected abstract T buildBean();

    public T build() {
        T bean = buildBean();
        fieldValidator.validate(bean, validateFields);

        return bean;
    }

    private static void useFieldValidator(FieldValidator fieldValidator) {
        Builder.fieldValidator = fieldValidator;
    }

    static {
        useFieldValidator(new FieldValidator());
    }
}
