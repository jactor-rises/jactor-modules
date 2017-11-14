package com.github.jactorrises.model.domain.user;

import com.github.jactorrises.client.datatype.EmailAddress;
import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.client.domain.User;
import com.github.jactorrises.model.domain.PersistentDomain;
import com.github.jactorrises.model.domain.person.PersonDomain;
import com.github.jactorrises.model.persistence.entity.user.UserEntity;

public class UserDomain extends PersistentDomain<UserEntity, Long> implements User {

    private final UserEntity userEntity;

    public UserDomain(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    @Override public UserName getUserName() {
        return userEntity.getUserName();
    }

    @Override public PersonDomain getPerson() {
        return userEntity.getPerson() != null ? new PersonDomain(userEntity.getPerson()) : null;
    }

    @Override public EmailAddress getEmailAddress() {
        return userEntity.getEmailAddress();
    }

    @Override public boolean isUserNameEmailAddress() {
        return userEntity.isUserNameEmailAddress();
    }

    @Override public UserEntity getEntity() {
        return userEntity;
    }

    public static UserBuilder aUser() {
        return new UserBuilder();
    }
}
