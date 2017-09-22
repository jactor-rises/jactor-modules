package com.github.jactorrises.model.domain.person;

import com.github.jactorrises.model.Builder;
import com.github.jactorrises.model.domain.address.AddressBuilder;
import com.github.jactorrises.model.domain.address.AddressDomain;
import com.github.jactorrises.model.persistence.entity.person.PersonEntity;
import com.github.jactorrises.model.persistence.entity.person.PersonEntityBuilder;

import java.util.Optional;

import static com.github.jactorrises.model.persistence.entity.person.PersonEntity.aPerson;
import static java.util.Collections.singletonList;

public final class PersonBuilder extends Builder<PersonDomain> {
    static final String AN_ADDRESS_MUST_BE_PRESENT = "An address must be present";

    private PersonEntityBuilder personEntityBuilder = aPerson();

    PersonBuilder() {
        super(singletonList(
                domain -> domain.getAddress() != null ? Optional.empty() : Optional.of(AN_ADDRESS_MUST_BE_PRESENT)
        ));
    }

    public PersonBuilder with(AddressDomain address) {
        personEntityBuilder.with(address.getEntity());
        return this;
    }

    public PersonBuilder with(AddressBuilder address) {
        return with(address.build());
    }

    public PersonBuilder withDescription(String description) {
        personEntityBuilder.withDescription(description);
        return this;
    }

    @Override protected PersonDomain buildBean() {
        return new PersonDomain(personEntityBuilder.build());
    }

    public static PersonDomain build(PersonEntity person) {
        return new PersonDomain(person);
    }
}
