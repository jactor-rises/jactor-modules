package com.github.jactorrises.model.domain.person;

import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.client.domain.Person;
import com.github.jactorrises.client.persistence.dto.PersonDto;
import com.github.jactorrises.model.domain.PersistentDomain;
import com.github.jactorrises.model.domain.address.AddressDomain;
import com.github.jactorrises.model.domain.user.UserDomain;

import java.util.Locale;

public class PersonDomain extends PersistentDomain<Long> implements Person {

    private final PersonDto personDto;

    public PersonDomain(PersonDto personDto) {
        this.personDto = personDto;
    }

    @Override public String getDescription() {
        return personDto.getDescription();
    }

    @Override public UserDomain getUser() {
        return new UserDomain(personDto.getUser());
    }

    @Override public Name getFirstName() {
        return personDto.getFirstName();
    }

    @Override public Name getSurname() {
        return personDto.getSurname();
    }

    @Override public Locale getLocale() {
        return personDto.getLocale();
    }

    @Override public AddressDomain getAddress() {
        return personDto.getAddress() != null ? new AddressDomain(personDto.getAddress()) : null;
    }

    @Override public PersonDto getDto() {
        return personDto;
    }

    public static PersonBuilder aPerson() {
        return new PersonBuilder();
    }
}
