package com.github.jactorrises.model.domain.person;

import com.github.jactorrises.model.DomainBuilder;
import com.github.jactorrises.model.domain.address.AddressDomain;
import com.github.jactorrises.model.domain.address.AddressDomainBuilder;
import com.github.jactorrises.model.persistence.entity.person.PersonEntity;

import java.util.Optional;

import static java.util.Collections.singletonList;

public final class PersonDomainBuilder extends DomainBuilder<PersonDomain> {
    static final String AN_ADDRESS_MUST_BE_PRESENT = "An address must be present";

    private PersonEntity personEntity = new PersonEntity();

    PersonDomainBuilder() {
        super(singletonList(
                domain -> domain.getAddress() != null ? Optional.empty() : Optional.of(AN_ADDRESS_MUST_BE_PRESENT)
        ));
    }

    @Override protected PersonDomain buildBeforeValidation() {
        return new PersonDomain(personEntity);
    }

    public PersonDomainBuilder with(AddressDomain address) {
        personEntity.setAddressEntity(address.getEntity());
        return this;
    }

    public PersonDomainBuilder with(AddressDomainBuilder address) {
        return with(address.build());
    }

    public PersonDomainBuilder withDescription(String description) {
        personEntity.setDescription(description);
        return this;
    }

    public static PersonDomain build(PersonEntity person) {
        return new PersonDomain(person);
    }
}
