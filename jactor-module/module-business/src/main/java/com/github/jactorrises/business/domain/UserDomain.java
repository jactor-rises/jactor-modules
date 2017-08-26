package com.github.jactorrises.business.domain;

import com.github.jactorrises.business.domain.builder.UserDomainBuilder;
import nu.hjemme.client.datatype.EmailAddress;
import nu.hjemme.client.datatype.UserName;
import nu.hjemme.client.domain.Person;
import nu.hjemme.client.domain.User;
import nu.hjemme.persistence.client.UserEntity;

public class UserDomain extends PersistentDomain<UserEntity, Long> implements User {

    public UserDomain(UserEntity userEntity) {
        super(userEntity);
    }

    @Override public String getPassword() {
        return getEntity().getPassword();
    }

    @Override public UserName getUserName() {
        return getEntity().getUserName();
    }

    @Override public Person getPerson() {
        return getEntity().getPerson();
    }

    @Override public EmailAddress getEmailAddress() {
        return getEntity().getEmailAddress();
    }

    @Override public boolean isUserNameEmailAddress() {
        return getEntity().isUserNameEmailAddress();
    }

    public static UserDomainBuilder aUser() {
        return UserDomainBuilder.init();
    }
}
