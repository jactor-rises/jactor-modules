package com.gitlab.jactor.rises.model.domain;

import com.gitlab.jactor.rises.io.datatype.EmailAddress;
import com.gitlab.jactor.rises.io.datatype.Username;

public interface User extends Persistent {

    Username getUsername();

    Person getPerson();

    EmailAddress getEmailAddress();

    default boolean isUserNameEmailAddress() {
        return getEmailAddress() != null && getEmailAddress().isSameAs(getUsername());
    }
}
