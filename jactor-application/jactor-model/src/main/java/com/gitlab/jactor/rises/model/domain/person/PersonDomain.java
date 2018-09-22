package com.gitlab.jactor.rises.model.domain.person;

import com.gitlab.jactor.rises.commons.datatype.Name;
import com.gitlab.jactor.rises.model.domain.Person;
import com.gitlab.jactor.rises.commons.dto.PersonDto;
import com.gitlab.jactor.rises.model.domain.PersistentDomain;
import com.gitlab.jactor.rises.model.domain.address.AddressDomain;

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
