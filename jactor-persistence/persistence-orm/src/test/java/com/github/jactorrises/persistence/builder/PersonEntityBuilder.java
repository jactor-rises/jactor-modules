package com.github.jactorrises.persistence.builder;

import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.persistence.entity.address.AddressOrm;
import com.github.jactorrises.persistence.entity.person.PersonOrm;
import com.github.jactorrises.persistence.entity.user.UserOrm;

import java.util.Locale;

public class PersonEntityBuilder {
    private AddressOrm addressOrm;
    private Locale locale;
    private Name firstName;
    private Name surname;
    private String description;
    private UserOrm userEntity;

    private PersonEntityBuilder() {
    }

    public PersonEntityBuilder with(AddressOrm entity) {
        addressOrm = entity;
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

    public PersonOrm build() {
        PersonOrm personOrm = new PersonOrm();
        personOrm.setAddressEntity(addressOrm);
        personOrm.setDescription(description);
        personOrm.setFirstName(firstName);
        personOrm.setSurname(surname);
        personOrm.setLocale(locale);
        personOrm.setUserEntity(userEntity);

        return personOrm;
    }

    public static PersonEntityBuilder aPerson() {
        return new PersonEntityBuilder();
    }
}
