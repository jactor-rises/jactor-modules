package com.github.jactorrises.model.internal.domain;

import com.github.jactorrises.model.internal.domain.builder.PersonDomainBuilder;
import com.github.jactorrises.client.datatype.Description;
import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.client.domain.Address;
import com.github.jactorrises.client.domain.Person;
import com.github.jactorrises.model.internal.persistence.entity.person.PersonEntity;

import java.util.Locale;

public class PersonDomain extends PersistentDomain<PersonEntity, Long> implements Person {

    public PersonDomain(PersonEntity personEntity) {
        super(personEntity);
    }

    @Override public Description getDescription() {
        return getEntity().getDescription();
    }

    @Override public UserDomain getUser() {
        return new UserDomain(getEntity().getUser());
    }

    @Override public Name getFirstName() {
        return getEntity().getFirstName();
    }

    @Override public Name getLastName() {
        return getEntity().getLastName();
    }

    @Override public Locale getLocale() {
        return getEntity().getLocale();
    }

    @Override public Address getAddress() {
        return getEntity().getAddress();
    }

    public static PersonDomainBuilder aPerson() {
        return PersonDomainBuilder.init();
    }
}
