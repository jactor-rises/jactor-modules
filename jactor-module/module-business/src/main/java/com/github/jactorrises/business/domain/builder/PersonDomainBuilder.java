package com.github.jactorrises.business.domain.builder;

import com.github.jactorrises.business.domain.PersonDomain;
import com.github.jactorrises.business.domain.AddressDomain;
import com.github.jactorrises.persistence.client.PersonEntity;
import com.github.jactorrises.persistence.boot.facade.PersistentDataService;

import java.util.Optional;

import static java.util.Collections.singletonList;

public class PersonDomainBuilder extends DomainBuilder<PersonDomain> {
    static final String AN_ADDRESS_MUST_BE_PRESENT = "An address must be present";

    private PersonEntity personEntity = PersistentDataService.getInstance().provideInstanceFor(PersonEntity.class);

    private PersonDomainBuilder() {
        super(singletonList(
                domain -> domain.getAddress() != null ? Optional.empty() : Optional.of(AN_ADDRESS_MUST_BE_PRESENT)
        ));
    }

    @Override protected PersonDomain addhRequiredFields() {
        return new PersonDomain(personEntity);
    }

    public PersonDomainBuilder with(AddressDomain address) {
        personEntity.setAddressEntity(address.getEntity());
        return this;
    }

    public PersonDomainBuilder with(AddressDomainBuilder address) {
        return with(address.build());
    }

    public PersonDomainBuilder withDescriptionAs(String description) {
        personEntity.setDescription(description);
        return this;
    }

    public static PersonDomainBuilder init() {
        return new PersonDomainBuilder();
    }
}
