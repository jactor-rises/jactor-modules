package com.github.jactor.rises.model.domain.person;

import com.github.jactor.rises.client.datatype.Name;
import com.github.jactor.rises.client.domain.Person;
import com.github.jactor.rises.client.dto.PersonDto;
import com.github.jactor.rises.model.domain.PersistentDomain;
import com.github.jactor.rises.model.domain.address.AddressDomain;
import com.github.jactor.rises.model.domain.user.UserDomain;

import java.util.Locale;
import java.util.Optional;

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
        return Optional.ofNullable(personDto.getFirstName()).map(Name::new).orElse(null);
    }

    @Override public Name getSurname() {
        return Optional.ofNullable(personDto.getSurname()).map(Name::new).orElse(null);
    }

    @Override public Locale getLocale() {
        return Optional.ofNullable(personDto.getLocale()).map(Locale::new).orElse(null);
    }

    @Override public AddressDomain getAddress() {
        return Optional.ofNullable(personDto.getAddress()).map(AddressDomain::new).orElse(null);
    }

    @Override public PersonDto getDto() {
        return personDto;
    }

    public static PersonBuilder aPerson() {
        return new PersonBuilder();
    }
}
