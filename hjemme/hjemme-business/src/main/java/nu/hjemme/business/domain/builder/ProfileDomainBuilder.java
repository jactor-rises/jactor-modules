package nu.hjemme.business.domain.builder;

import nu.hjemme.business.domain.ProfileDomain;
import nu.hjemme.persistence.db.AddressEntityImpl;
import nu.hjemme.persistence.db.ProfileEntityImpl;
import org.apache.commons.lang.Validate;

/** @author Tor Egil Jacobsen */
public class ProfileDomainBuilder extends DomainBuilder<ProfileDomain> {
    static final String AN_ADDRESS_MUST_BE_PRESENT = "An address must be present";
    static final String THE_FIRST_NAME_CANNOT_BE_NULL = "The first cannot be null";
    static final String THE_LAST_NAME_CANNOT_BE_NULL = "The last cannot be null";

    private ProfileEntityImpl profileEntity = new ProfileEntityImpl();

    @Override
    protected ProfileDomain buildInstance() {
        return new ProfileDomain(profileEntity);
    }

    @Override
    protected void validate() {
        Validate.notNull(profileEntity.getFirstName(), THE_FIRST_NAME_CANNOT_BE_NULL);
        Validate.notNull(profileEntity.getLastName(), THE_LAST_NAME_CANNOT_BE_NULL);
        Validate.notNull(profileEntity.getAddress(), AN_ADDRESS_MUST_BE_PRESENT);
    }

    public static ProfileDomainBuilder init() {
        return new ProfileDomainBuilder();
    }

    public ProfileDomainBuilder appendLastName(String lastName) {
        profileEntity.addLastName(lastName);
        return this;
    }

    public ProfileDomainBuilder appendFirstName(String firstName) {
        profileEntity.addFirstName(firstName);
        return this;
    }

    public ProfileDomainBuilder appendAddress(AddressEntityImpl addressEntity) {
        profileEntity.addAddressEntity(addressEntity);
        return this;
    }

    public ProfileDomainBuilder appendDescription(String description) {
        profileEntity.setDescription(description);
        return this;
    }

    public ProfileEntityImpl getValidatedProfileEntity() {
        validate();
        return profileEntity;
    }
}