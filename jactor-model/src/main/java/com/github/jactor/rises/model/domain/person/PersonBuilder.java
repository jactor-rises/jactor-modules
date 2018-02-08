package com.github.jactor.rises.model.domain.person;

import com.github.jactorrises.client.dto.PersonDto;
import com.github.jactor.rises.commons.builder.AbstractBuilder;
import com.github.jactor.rises.model.domain.address.AddressBuilder;
import com.github.jactor.rises.model.domain.address.AddressDomain;

import java.util.Optional;

import static com.github.jactor.rises.commons.builder.ValidInstance.collectMessages;
import static com.github.jactor.rises.commons.builder.ValidInstance.fetchMessageIfFieldNotPresent;

public final class PersonBuilder extends AbstractBuilder<PersonDomain> {
    private PersonDto personDto = new PersonDto();

    PersonBuilder() {
        super(PersonBuilder::validateInstance);
    }

    public PersonBuilder with(AddressDomain address) {
        personDto.setAddress(address.getDto());
        return this;
    }

    public PersonBuilder with(AddressBuilder address) {
        return with(address.build());
    }

    public PersonBuilder withDescription(String description) {
        personDto.setDescription(description);
        return this;
    }

    PersonBuilder withFirstName(String firstName) {
        personDto.setFirstName(firstName);
        return this;
    }

    public PersonBuilder withSurname(String surname) {
        personDto.setSurname(surname);
        return this;
    }

    PersonBuilder withLocale(String locale) {
        personDto.setLocale(locale);
        return this;
    }

    @Override protected PersonDomain buildBean() {
        return new PersonDomain(personDto);
    }

    private static Optional<String> validateInstance(PersonDomain personDomain) {
        return collectMessages(
                fetchMessageIfFieldNotPresent("address", personDomain.getAddress()),
                fetchMessageIfFieldNotPresent("surname", personDomain.getSurname())
        );
    }

    public static PersonDomain build(PersonDto person) {
        return new PersonDomain(person);
    }
}
