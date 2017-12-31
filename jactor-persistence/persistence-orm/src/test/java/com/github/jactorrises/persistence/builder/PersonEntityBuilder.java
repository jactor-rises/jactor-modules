package com.github.jactorrises.persistence.builder;

import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.persistence.entity.address.AddressEntity;
import com.github.jactorrises.persistence.entity.person.PersonEntity;
import com.github.jactorrises.persistence.entity.user.UserEntity;

import java.util.Locale;

public class PersonEntityBuilder {
    private AddressEntity addressEntity;
    private Locale locale;
    private Name firstName;
    private Name surname;
    private String description;
    private UserEntity userEntity;

    private PersonEntityBuilder() {
    }

    public PersonEntityBuilder with(AddressEntity entity) {
        addressEntity = entity;
        return this;
    }

    public PersonEntityBuilder with(AddressEntityBuilder addressEntityBuilder) {
        return with(addressEntityBuilder.build());
    }

    public PersonEntityBuilder with(UserEntityBuilder userEntityBuilder) {
        userEntity = userEntityBuilder.build();
        return this;
    }

    public PersonEntityBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public PersonEntityBuilder withFirstName(String firstName) {
        this.firstName = new Name(firstName);
        return this;
    }

    public PersonEntityBuilder withSurname(String surname) {
        this.surname = new Name(surname);
        return this;
    }

    public PersonEntityBuilder withLocale(String locale) {
        this.locale = new Locale(locale);
        return this;
    }

    public PersonEntity build() {
        PersonEntity personEntity = new PersonEntity();
        personEntity.setAddressEntity(addressEntity);
        personEntity.setDescription(description);
        personEntity.setFirstName(firstName);
        personEntity.setSurname(surname);
        personEntity.setLocale(locale);
        personEntity.setUserEntity(userEntity);

        return personEntity;
    }

    public static PersonEntityBuilder aPerson() {
        return new PersonEntityBuilder();
    }
}
