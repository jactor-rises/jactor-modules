package com.github.jactorrises.persistence.builder;

import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.persistence.client.entity.AddressEntity;
import com.github.jactorrises.persistence.client.entity.PersonEntity;
import com.github.jactorrises.persistence.entity.address.AddressOrm;
import com.github.jactorrises.persistence.entity.person.PersonOrm;
import com.github.jactorrises.persistence.entity.user.UserOrm;

import java.util.Locale;

import static com.github.jactorrises.persistence.builder.AddressEntityBuilder.anAddress;

public class PersonEntityBuilder {
    private AddressOrm addressOrm;
    private Locale locale;
    private Name firstName;
    private Name surname;
    private String description;
    private UserOrm userEntity;

    private PersonEntityBuilder() {
    }

    public PersonEntityBuilder with(AddressEntity entity) {
        if (entity instanceof AddressOrm) {
            addressOrm = (AddressOrm) entity;
            return this;
        }

        return with(anAddress()
                .withAddressLine1(entity.getAddressLine1())
                .withAddressLine2(entity.getAddressLine2())
                .withAddressLine3(entity.getAddressLine3())
                .withCity(entity.getCity())
                .withCountryCode(entity.getCountry().getCountryCode())
                .withZipCode(entity.getZipCode())
        );
    }

    public PersonEntityBuilder with(AddressEntityBuilder addressEntityBuilder) {
        return with(addressEntityBuilder.build());
    }

    public PersonEntityBuilder with(UserEntityBuilder userEntityBuilder) {
        userEntity = (UserOrm) userEntityBuilder.build();
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
