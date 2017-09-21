package com.github.jactorrises.model.domain.user;

import com.github.jactorrises.client.datatype.EmailAddress;
import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.client.domain.User;
import com.github.jactorrises.model.domain.PersistentDomain;
import com.github.jactorrises.model.domain.person.PersonBuilder;
import com.github.jactorrises.model.domain.person.PersonDomain;
import com.github.jactorrises.model.persistence.entity.person.PersonEntity;
import com.github.jactorrises.model.persistence.entity.user.UserEntity;

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

    @Override public PersonDomain getPerson() {
        return personEntity() != null ? PersonBuilder.build(personEntity()) : null;
    }

    private PersonEntity personEntity() {
        return getEntity().getPerson();
    }

    @Override public EmailAddress getEmailAddress() {
        return getEntity().getEmailAddress();
    }

    @Override public boolean isUserNameEmailAddress() {
        return getEntity().isUserNameEmailAddress();
    }

    public static UserBuilder aUser() {
        return new UserBuilder();
    }
}
