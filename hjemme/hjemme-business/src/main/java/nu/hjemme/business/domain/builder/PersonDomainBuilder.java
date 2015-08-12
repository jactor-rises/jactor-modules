package nu.hjemme.business.domain.builder;

import nu.hjemme.business.domain.PersonDomain;
import nu.hjemme.client.datatype.Name;
import nu.hjemme.persistence.AddressEntity;
import nu.hjemme.persistence.PersonEntity;
import org.apache.commons.lang.Validate;

/** @author Tor Egil Jacobsen */
public class PersonDomainBuilder extends DomainBuilder<PersonDomain> {
    static final String AN_ADDRESS_MUST_BE_PRESENT = "An address must be present";
    static final String THE_FIRST_NAME_CANNOT_BE_NULL = "The first cannot be null";
    static final String THE_LAST_NAME_CANNOT_BE_NULL = "The last cannot be null";

    private PersonEntity personEntity = new PersonEntity();

    public PersonDomainBuilder appendAddress(AddressEntity addressEntity) {
        personEntity.setAddress(addressEntity);
        return this;
    }

    public PersonDomainBuilder appendFirstName(String firstName) {
        personEntity.setFirstName(new Name(firstName));
        return this;
    }

    public PersonDomainBuilder appendLastName(String lastName) {
        personEntity.setLastName(new Name(lastName));
        return this;
    }

    @Override
    protected PersonDomain buildInstance() {
        return new PersonDomain(personEntity);
    }

    @Override
    protected void validate() {
        Validate.notNull(personEntity.getFirstName(), THE_FIRST_NAME_CANNOT_BE_NULL);
        Validate.notNull(personEntity.getLastName(), THE_LAST_NAME_CANNOT_BE_NULL);
        Validate.notNull(personEntity.getAddress(), AN_ADDRESS_MUST_BE_PRESENT);
    }

    public static PersonDomainBuilder init() {
        return new PersonDomainBuilder();
    }
}
