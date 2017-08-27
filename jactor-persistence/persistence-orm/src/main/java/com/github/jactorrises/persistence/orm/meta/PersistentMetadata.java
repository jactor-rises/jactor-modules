package com.github.jactorrises.persistence.orm.meta;

public final class PersistentMetadata {
    private PersistentMetadata() {}

    public final static String ID = "ID"; // all persistent classes need an id

    public static final String CREATION_TIME = "CREATION_TIME"; // creation time of this persisted data
    public static final String CREATED_BY = "CREATED_BY"; // who created the persisted data
    public static final String UPDATED_TIME = "UPDATED_TIME"; // updated time of this persisted data
    public static final String UPDATED_BY = "UPDATED_BY"; // who updated the persisted data

}
