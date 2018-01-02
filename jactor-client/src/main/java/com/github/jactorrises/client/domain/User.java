package com.github.jactorrises.client.domain;

import com.github.jactorrises.client.datatype.EmailAddress;
import com.github.jactorrises.client.datatype.UserName;

public interface User extends Persistent {

    UserName getUserName();

    Person getPerson();

    EmailAddress getEmailAddress();

    default boolean isUserNameEmailAddress() {
        return getEmailAddress().isSameAs(getUserName());
    }
}
