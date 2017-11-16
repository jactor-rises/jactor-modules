package com.github.jactorrises.model.persistence.entity.person;

import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.model.persistence.entity.address.AddressEntityBuilder;
import com.github.jactorrises.model.persistence.entity.address.AddressOrm;
import com.github.jactorrises.model.persistence.entity.user.UserEntity;
import com.github.jactorrises.model.persistence.entity.user.UserEntityBuilder;
import com.github.jactorrises.persistence.client.entity.AddressEntity;

import java.util.Locale;

import static com.github.jactorrises.model.persistence.entity.address.AddressEntityBuilder.anAddress;

public class PersonEntityBuilder {
    private AddressOrm addressOrm;
    private Locale locale;
    private Name firstName;
    private Name surname;
    private String description;
    private UserEntity userEntity;

    PersonEntityBuilder() {
    }

    public PersonEntityBuilder with(AddressEntity entity) {
        if (entity instanceof AddressOrm) {
            return with((AddressOrm) entity);
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

    public PersonEntityBuilder with(AddressOrm addressOrm) {
        this.addressOrm = addressOrm;
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
        personEntity.setAddressEntity(addressOrm);
        personEntity.setDescription(description);
        personEntity.setFirstName(firstName);
        personEntity.setSurname(surname);
        personEntity.setLocale(locale);
        personEntity.setUserEntity(userEntity);

        return personEntity;
    }
}
