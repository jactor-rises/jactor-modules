package nu.hjemme.business.domain;

import nu.hjemme.business.domain.builder.PersonDomainBuilder;
import nu.hjemme.client.datatype.Description;
import nu.hjemme.client.datatype.Name;
import nu.hjemme.client.domain.Address;
import nu.hjemme.client.domain.Person;
import nu.hjemme.persistence.client.PersonEntity;

import java.util.Locale;

public class PersonDomain extends PersistentDomain<PersonEntity, Long> implements Person {

    public PersonDomain(PersonEntity personEntity) {
        super(personEntity);
    }

    @Override public Description getDescription() {
        return getEntity().getDescription();
    }

    @Override public UserDomain getUser() {
        return new UserDomain(getEntity().getUser());
    }

    @Override public Name getFirstName() {
        return getEntity().getFirstName();
    }

    @Override public Name getLastName() {
        return getEntity().getLastName();
    }

    @Override public Locale getLocale() {
        return getEntity().getLocale();
    }

    @Override public Address getAddress() {
        return getEntity().getAddress();
    }

    public static PersonDomainBuilder aPerson() {
        return PersonDomainBuilder.init();
    }
}
