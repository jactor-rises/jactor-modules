package com.github.jactorrises.persistence.entity.user;

import com.github.jactorrises.commons.builder.AbstractBuilder;
import com.github.jactorrises.persistence.entity.person.PersonEntity;
import com.github.jactorrises.persistence.entity.person.PersonEntityBuilder;

public class UserEntityBuilder extends AbstractBuilder<UserEntity> {
    private String emailAddress;
    private PersonEntity person;
    private String userName;

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
        this.emailAddress = emailAddress;
        return this;
    }

    public UserEntityBuilder withUserName(String userName) {
        this.userName = userName;
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
