package com.github.jactorrises.model.persistence.entity.user;

import com.github.jactorrises.client.datatype.EmailAddress;
import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.model.persistence.entity.person.PersonEntity;
import com.github.jactorrises.model.persistence.entity.person.PersonEntityBuilder;

public class UserEntityBuilder {
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

    public UserEntity build() {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmailAddress(emailAddress);
        userEntity.setPersonEntity(person);
        userEntity.setUserName(userName);

        return userEntity;
    }
}
