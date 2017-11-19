package com.github.jactorrises.persistence.repository;

import com.github.jactorrises.persistence.entity.PersistentEntity;

public class RepositoryCriterion<T extends PersistentEntity> {
    private final Class<T> persistentClass;
    private Long id;

    public RepositoryCriterion(Class<T> persistentClass) {
        this.persistentClass = persistentClass;
    }

    Class<T> getPersistentClass() {
        return persistentClass;
    }

    public Long getId() {
        return id;
    }

    public RepositoryCriterion<T> with(Long id) {
        this.id = id;
        return this;
    }
}
