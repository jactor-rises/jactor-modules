package com.github.jactorrises.persistence.client.entity;

import com.github.jactorrises.client.datatype.EmailAddress;
import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.client.domain.User;

public interface UserEntity extends User, PersistentEntity<Long> {

    void setEmailAddress(EmailAddress emailAddress);

    void setUserName(UserName userName);

    void setPersonEntity(PersonEntity personEntity);
}
