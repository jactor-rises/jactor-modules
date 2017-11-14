package com.github.jactorrises.model.domain;


/**
 * A builder which validates fields on a domain before returning the instance using a {@link DomainValidator}
 *
 * @param <T> type of domain to build
 */
public abstract class Builder<T> {
    private final DomainValidator<T> domainValidator;

    protected Builder(DomainValidator<T> domainValidator) {
        this.domainValidator = domainValidator;
    }

    protected abstract T buildDomain();

    public T build() {
        T domain = buildDomain();
        domainValidator.runOn(domain);

        return domain;
    }
}
