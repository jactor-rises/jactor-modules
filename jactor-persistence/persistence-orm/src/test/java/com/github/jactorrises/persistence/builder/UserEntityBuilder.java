package com.github.jactorrises.persistence.builder;

import com.github.jactorrises.client.datatype.EmailAddress;
import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.persistence.entity.person.PersonEntity;
import com.github.jactorrises.persistence.entity.user.UserEntity;

public class UserEntityBuilder {
    private EmailAddress emailAddress;
    private PersonEntity person;
    private UserName userName;

    private UserEntityBuilder() {
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

    public UserEntity build() {
        UserEntity useruserEntity = new UserEntity();
        useruserEntity.setEmailAddress(emailAddress);
        useruserEntity.setPersonEntity(person);
        useruserEntity.setUserName(userName);

        return useruserEntity;
    }

    public static UserEntityBuilder aUser() {
        return new UserEntityBuilder();
    }
}
