package com.github.jactor.rises.persistence.entity.user;

import com.github.jactor.rises.commons.builder.AbstractBuilder;
import com.github.jactor.rises.commons.builder.MissingFields;
import com.github.jactor.rises.persistence.entity.person.PersonEntity;
import com.github.jactor.rises.persistence.entity.person.PersonEntityBuilder;

import java.util.Optional;

public class UserEntityBuilder extends AbstractBuilder<UserEntity> {
    private String emailAddress;
    private PersonEntity person;
    private String username;

    UserEntityBuilder() {
        super(UserEntityBuilder::validate);
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

    public UserEntityBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    @Override public UserEntity buildBean() {
        UserEntity useruserEntity = new UserEntity();
        useruserEntity.setEmailAddress(emailAddress);
        useruserEntity.setPersonEntity(person);
        useruserEntity.setUsername(username);

        return useruserEntity;
    }

    private static Optional<MissingFields> validate(UserEntity userEntity, MissingFields missingFields) {
        missingFields.addInvalidFieldWhenBlank("username", userEntity.getUsername());
        missingFields.addInvalidFieldWhenNoValue("personEntity", userEntity.getPerson());

        return missingFields.presentWhenFieldsAreMissing();
    }
}
