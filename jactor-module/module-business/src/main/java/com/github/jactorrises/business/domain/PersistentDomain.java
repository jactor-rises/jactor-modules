package com.github.jactorrises.business.domain;

import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.client.domain.Persistent;
import org.apache.commons.lang3.Validate;

import java.time.LocalDateTime;

public abstract class PersistentDomain<T extends Persistent<I>, I> implements Persistent<I> {
    static final String THE_ENTITY_ON_THE_DOMAIN_CANNOT_BE_NULL = "The Entity on the domain cannot be null!";

    private final T entity;

    PersistentDomain(T entity) {
        Validate.notNull(entity, THE_ENTITY_ON_THE_DOMAIN_CANNOT_BE_NULL);
        this.entity = entity;
    }

    public T getEntity() {
        return entity;
    }

    @Override public I getId() {
        return entity.getId();
    }

    @Override public Name getCreatedBy() {
        return entity.getCreatedBy();
    }

    @Override public LocalDateTime getCreationTime() {
        return entity.getCreationTime();
    }

    @Override public Name getUpdatedBy() {
        return entity.getUpdatedBy();
    }

    @Override public LocalDateTime getUpdatedTime() {
        return entity.getUpdatedTime();
    }
}
