package com.github.jactorrises.model.internal.persistence.entity.user;

final class UserMetadata {

    private UserMetadata() {}

    /** The database table for a user */
    static final String USER_TABLE = "T_USER";

    /** The password for the user */
    static final String PASSWORD = "PASSWORD"; // NOSONAR, is not a hardcoded password...
    /** The user name of the user */
    static final String USER_NAME = "USER_NAME";
    /** The email address to the the user */
    static final String EMAIL = "EMAIL";
    /** If the user uses the email address as an user name */
    static final String EMAIL_AS_NAME = "EMAIL_AS_NAME";
    /** Foreign key to the person */
    static final String PERSON_ID = "PERSON_ID";
}
