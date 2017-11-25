package com.github.jactorrises.model.domain;

import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.client.domain.Persistent;
import org.apache.commons.lang3.Validate;

import java.io.Serializable;
import java.time.LocalDateTime;

public abstract class PersistentDomain<T extends Serializable> implements Persistent<T> {
    static final String THE_PERSISTENT_DATA_ON_THE_DOMAIN_CANNOT_BE_NULL = "The persistent data the domain cannot be null!";

    private Persistent<T> fetchPersistence() {
        Persistent<T> entity = getPersistence();
        Validate.notNull(entity, THE_PERSISTENT_DATA_ON_THE_DOMAIN_CANNOT_BE_NULL);

        return entity;
    }

    public abstract Persistent<T> getPersistence();

    @Override public T getId() {
        return fetchPersistence().getId();
    }

    @Override public Name getCreatedBy() {
        return fetchPersistence().getCreatedBy();
    }

    @Override public LocalDateTime getCreationTime() {
        return fetchPersistence().getCreationTime();
    }

    @Override public Name getUpdatedBy() {
        return fetchPersistence().getUpdatedBy();
    }

    @Override public LocalDateTime getUpdatedTime() {
        return fetchPersistence().getUpdatedTime();
    }
}
