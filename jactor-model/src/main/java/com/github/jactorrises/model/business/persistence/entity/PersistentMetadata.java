package com.github.jactorrises.model.business.persistence.entity;

final class PersistentMetadata {

    private PersistentMetadata() {
    }

    static final String ID = "ID"; // all persistent classes need an id

    static final String CREATION_TIME = "CREATION_TIME"; // creation time of this persisted data
    static final String CREATED_BY = "CREATED_BY"; // who created the persisted data
    static final String UPDATED_TIME = "UPDATED_TIME"; // updated time of this persisted data
    static final String UPDATED_BY = "UPDATED_BY"; // who updated the persisted data
}
