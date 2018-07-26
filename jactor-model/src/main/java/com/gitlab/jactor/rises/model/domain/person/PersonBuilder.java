package com.gitlab.jactor.rises.model.domain.person;

import com.gitlab.jactor.rises.commons.dto.PersonDto;
import com.gitlab.jactor.rises.commons.builder.AbstractBuilder;
import com.gitlab.jactor.rises.commons.builder.MissingFields;
import com.gitlab.jactor.rises.model.domain.address.AddressBuilder;
import com.gitlab.jactor.rises.model.domain.address.AddressDomain;

import java.util.Optional;

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

    private static Optional<MissingFields> validateInstance(PersonDomain personDomain, MissingFields missingFields) {
        missingFields.addInvalidFieldWhenNoValue("address", personDomain.getAddress());
        missingFields.addInvalidFieldWhenNoValue("surname", personDomain.getSurname());

        return missingFields.presentWhenFieldsAreMissing();
    }

    public static PersonDomain build(PersonDto person) {
        return new PersonDomain(person);
    }
}
