package com.github.jactorrises.model;

import com.github.jactorrises.model.domain.DomainValidater;

import java.util.List;

/**
 * A builder which validates fields on a domain before returning the instance using a {@link DomainValidater}
 *
 * @param <T> type of domain to build
 */
public abstract class Builder<T> {
    private static FieldValidator fieldValidator;
    private final List<FieldValidator.ValidateField<T>> validateFields;
    private final DomainValidater<T> domainValidater;

    protected Builder(List<FieldValidator.ValidateField<T>> validateFields) {
        this.validateFields = validateFields;
        domainValidater = null;
    }

    protected Builder(DomainValidater<T> domainValidater) {
        this.domainValidater = domainValidater;
        validateFields = null;
    }

    protected abstract T buildBean();

    public T build() {
        T domain = buildBean();

        if (domainValidater != null) {
            domainValidater.runOn(domain);
        } else {
            fieldValidator.validate(domain, validateFields);
        }

        return domain;
    }

    private static void useFieldValidator(FieldValidator fieldValidator) {
        Builder.fieldValidator = fieldValidator;
    }

    static {
        useFieldValidator(new FieldValidator());
    }
}
