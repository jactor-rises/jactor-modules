package com.github.jactorrises.model.domain.person;

import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.client.domain.Person;
import com.github.jactorrises.model.domain.PersistentDomain;
import com.github.jactorrises.model.domain.address.AddressDomain;
import com.github.jactorrises.model.domain.user.UserDomain;
import com.github.jactorrises.persistence.client.entity.AddressEntity;
import com.github.jactorrises.persistence.client.entity.PersonEntity;
import com.github.jactorrises.persistence.client.entity.UserEntity;

import java.util.Locale;

public class PersonDomain extends PersistentDomain<PersonEntity, Long> implements Person {

    private final PersonEntity personEntity;

    public PersonDomain(PersonEntity personEntity) {
        this.personEntity = personEntity;
    }

    @Override public String getDescription() {
        return personEntity.getDescription();
    }

    @Override public UserDomain getUser() {
        return new UserDomain((UserEntity) personEntity.getUser());
    }

    @Override public Name getFirstName() {
        return personEntity.getFirstName();
    }

    @Override public Name getSurname() {
        return personEntity.getSurname();
    }

    @Override public Locale getLocale() {
        return personEntity.getLocale();
    }

    @Override public AddressDomain getAddress() {
        return personEntity.getAddress() != null ? new AddressDomain((AddressEntity) personEntity.getAddress()) : null;
    }

    @Override public PersonEntity getEntity() {
        return personEntity;
    }

    public static PersonBuilder aPerson() {
        return new PersonBuilder();
    }
}
