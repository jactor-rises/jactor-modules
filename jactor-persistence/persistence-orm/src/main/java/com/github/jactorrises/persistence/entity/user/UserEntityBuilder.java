package com.github.jactorrises.persistence.entity.user;

import com.github.jactorrises.client.datatype.EmailAddress;
import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.commons.builder.AbstractBuilder;
import com.github.jactorrises.persistence.entity.person.PersonEntity;
import com.github.jactorrises.persistence.entity.person.PersonEntityBuilder;
import com.github.jactorrises.persistence.entity.user.UserEntity;

public class UserEntityBuilder extends AbstractBuilder<UserEntity> {
    private EmailAddress emailAddress;
    private PersonEntity person;
    private UserName userName;

    UserEntityBuilder() {
    }

    public UserEntityBuilder with(PersonEntity person) {
        this.person = person;
        return this;
    }

    public UserEntityBuilder with(PersonEntityBuilder personEntityBuilder) {
        return with(personEntityBuilder.build());
    }

    public UserEntityBuilder withEmailAddress(String emailAddress) {
        this.emailAddress = new EmailAddress(emailAddress);
        return this;
    }

    public UserEntityBuilder withUserName(String userName) {
        this.userName = new UserName(userName);
        return this;
    }

   @Override public UserEntity buildBean() {
        UserEntity useruserEntity = new UserEntity();
        useruserEntity.setEmailAddress(emailAddress);
        useruserEntity.setPersonEntity(person);
        useruserEntity.setUserName(userName);

        return useruserEntity;
    }
}
