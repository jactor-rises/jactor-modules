package com.github.jactorrises.model.domain;

import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.client.domain.Persistent;
import org.apache.commons.lang3.Validate;

import java.time.LocalDateTime;

public abstract class PersistentDomain<T extends Persistent<I>, I> implements Persistent<I> {
    static final String THE_ENTITY_ON_THE_DOMAIN_CANNOT_BE_NULL = "The Entity on the domain cannot be null!";

    private T fetchEntity() {
        T entity = getEntity();
        Validate.notNull(entity, THE_ENTITY_ON_THE_DOMAIN_CANNOT_BE_NULL);

        return entity;
    }

    public abstract T getEntity();

    @Override public I getId() {
        return fetchEntity().getId();
    }

    @Override public Name getCreatedBy() {
        return fetchEntity().getCreatedBy();
    }

    @Override public LocalDateTime getCreationTime() {
        return fetchEntity().getCreationTime();
    }

    @Override public Name getUpdatedBy() {
        return fetchEntity().getUpdatedBy();
    }

    @Override public LocalDateTime getUpdatedTime() {
        return fetchEntity().getUpdatedTime();
    }
}
