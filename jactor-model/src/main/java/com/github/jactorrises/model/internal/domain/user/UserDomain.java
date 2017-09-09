package com.github.jactorrises.model.internal.domain.user;

import com.github.jactorrises.model.internal.domain.PersistentDomain;
import com.github.jactorrises.client.datatype.EmailAddress;
import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.client.domain.Person;
import com.github.jactorrises.client.domain.User;
import com.github.jactorrises.model.internal.persistence.entity.user.UserEntity;

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
