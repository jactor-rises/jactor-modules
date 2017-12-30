package com.github.jactorrises.model.domain.person;

import com.github.jactorrises.commons.builder.AbstractBuilder;
import com.github.jactorrises.commons.builder.DomainValidator;
import com.github.jactorrises.model.domain.address.AddressBuilder;
import com.github.jactorrises.model.domain.address.AddressDomain;
import com.github.jactorrises.persistence.builder.PersonEntityBuilder;
import com.github.jactorrises.persistence.entity.person.PersonOrm;

import static com.github.jactorrises.persistence.builder.PersonEntityBuilder.aPerson;

public final class PersonBuilder extends AbstractBuilder<PersonDomain> {
    private PersonEntityBuilder personEntityBuilder = aPerson();

    PersonBuilder() {
        super(configureValidator());
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

    @Override protected PersonDomain buildDomain() {
        return new PersonDomain(personEntityBuilder.build());
    }

    private static DomainValidator<PersonDomain> configureValidator() {
        return new DomainValidator<PersonDomain>() {

            @Override public void validate(PersonDomain domain) {
                addIfInvalid(domain.getAddress() == null, "address", FieldValidation.REQUIRED);
                addIfInvalid(domain.getSurname() == null, "surname", FieldValidation.REQUIRED);
            }
        };
    }

    public static PersonDomain build(PersonOrm person) {
        return new PersonDomain(person);
    }
}
