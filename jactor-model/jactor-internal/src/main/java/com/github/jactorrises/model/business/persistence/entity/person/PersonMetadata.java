package com.github.jactorrises.model.business.persistence.entity.person;

final class PersonMetadata {
    private PersonMetadata() {}

    /** The database table for a person */
    static final String PERSON_TABLE = "T_PERSON";

    /** The address for this user/person */
    static final String ADDRESS_ID = "ADDRESS_ID";
    /** The description of a proile */
    static final String DESCRIPTION = "DESCRIPTION";
}
