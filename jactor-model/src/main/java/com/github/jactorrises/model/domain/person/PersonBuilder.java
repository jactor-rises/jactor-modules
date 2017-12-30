package com.github.jactorrises.model.domain.person;

import com.github.jactorrises.commons.builder.AbstractBuilder;
import com.github.jactorrises.model.domain.address.AddressBuilder;
import com.github.jactorrises.model.domain.address.AddressDomain;
import com.github.jactorrises.persistence.builder.PersonEntityBuilder;
import com.github.jactorrises.persistence.entity.person.PersonOrm;

import java.util.Optional;

import static com.github.jactorrises.commons.builder.ValidInstance.collectMessages;
import static com.github.jactorrises.commons.builder.ValidInstance.fetchMessageIfFieldNotPresent;
import static com.github.jactorrises.persistence.builder.PersonEntityBuilder.aPerson;

public final class PersonBuilder extends AbstractBuilder<PersonDomain> {
    private PersonEntityBuilder personEntityBuilder = aPerson();

    PersonBuilder() {
        super(PersonBuilder::validateInstance);
    }

    public PersonBuilder with(AddressDomain address) {
        personEntityBuilder.with(address.getPersistence());
        return this;
    }

    public PersonBuilder with(AddressBuilder address) {
        return with(address.build());
    }

    public PersonBuilder withDescription(String description) {
        personEntityBuilder.withDescription(description);
        return this;
    }

    PersonBuilder withFirstName(String firstName) {
        personEntityBuilder.withFirstName(firstName);
        return this;
    }

    public PersonBuilder withSurname(String surname) {
        personEntityBuilder.withSurname(surname);
        return this;
    }

    PersonBuilder withLocale(String locale) {
        personEntityBuilder.withLocale(locale);
        return this;
    }

    @Override protected PersonDomain buildBean() {
        return new PersonDomain(personEntityBuilder.build());
    }

    private static Optional<String> validateInstance(PersonDomain personDomain) {
        return collectMessages(
                fetchMessageIfFieldNotPresent("address", personDomain.getAddress()),
                fetchMessageIfFieldNotPresent("surname", personDomain.getSurname())
        );
    }

    public static PersonDomain build(PersonOrm person) {
        return new PersonDomain(person);
    }
}
