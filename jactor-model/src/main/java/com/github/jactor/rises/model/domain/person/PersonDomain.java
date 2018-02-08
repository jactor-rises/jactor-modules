package com.github.jactor.rises.model.domain.person;

import com.github.jactor.rises.client.datatype.Name;
import com.github.jactor.rises.client.domain.Person;
import com.github.jactor.rises.client.dto.PersonDto;
import com.github.jactor.rises.model.domain.PersistentDomain;
import com.github.jactor.rises.model.domain.address.AddressDomain;
import com.github.jactor.rises.model.domain.user.UserDomain;

import java.util.Locale;

public class PersonDomain extends PersistentDomain implements Person {

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
        return personDto.getFirstName() != null ? new Name(personDto.getFirstName()) : null;
    }

    @Override public Name getSurname() {
        return personDto.getSurname() != null ? new Name(personDto.getSurname()) : null;
    }

    @Override public Locale getLocale() {
        return personDto.getLocale() != null ? new Locale(personDto.getLocale()) : null;
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
