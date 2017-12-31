package com.github.jactorrises.persistence.builder;

import com.github.jactorrises.client.datatype.EmailAddress;
import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.persistence.entity.person.PersonEntity;
import com.github.jactorrises.persistence.entity.user.UserOrm;

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

    public UserOrm build() {
        UserOrm userOrm = new UserOrm();
        userOrm.setEmailAddress(emailAddress);
        userOrm.setPersonEntity(person);
        userOrm.setUserName(userName);

        return userOrm;
    }

    public static UserEntityBuilder aUser() {
        return new UserEntityBuilder();
    }
}
