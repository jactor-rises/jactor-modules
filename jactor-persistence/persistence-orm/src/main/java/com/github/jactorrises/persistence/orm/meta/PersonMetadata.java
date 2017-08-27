package com.github.jactorrises.persistence.orm.meta;

public final class PersonMetadata {
    private PersonMetadata() {}

    /** The database table for a person */
    public static final String PERSON_TABLE = "T_PERSON";

    /** The address for this user/person */
    public static final String ADDRESS_ID = "ADDRESS_ID";
    /** The description of a proile */
    public static final String DESCRIPTION = "DESCRIPTION";
}
