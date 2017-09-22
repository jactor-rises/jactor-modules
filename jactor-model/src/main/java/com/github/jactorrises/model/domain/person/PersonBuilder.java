package com.github.jactorrises.model.domain.person;

import com.github.jactorrises.model.Builder;
import com.github.jactorrises.model.domain.address.AddressBuilder;
import com.github.jactorrises.model.domain.address.AddressDomain;
import com.github.jactorrises.model.persistence.entity.person.PersonEntity;

import java.util.Optional;

import static com.github.jactorrises.model.persistence.entity.person.PersonEntity.aPerson;
import static java.util.Collections.singletonList;

public final class PersonBuilder extends Builder<PersonDomain> {
    static final String AN_ADDRESS_MUST_BE_PRESENT = "An address must be present";

    private PersonEntity personEntity = aPerson().build();

    PersonBuilder() {
        super(singletonList(
                domain -> domain.getAddress() != null ? Optional.empty() : Optional.of(AN_ADDRESS_MUST_BE_PRESENT)
        ));
    }

    @Override protected PersonDomain buildBean() {
        return new PersonDomain(personEntity);
    }

    public PersonBuilder with(AddressDomain address) {
        personEntity.setAddressEntity(address.getEntity());
        return this;
    }

    public PersonBuilder with(AddressBuilder address) {
        return with(address.build());
    }

    public PersonBuilder withDescription(String description) {
        personEntity.setDescription(description);
        return this;
    }

    public static PersonDomain build(PersonEntity person) {
        return new PersonDomain(person);
    }
}
