package nu.hjemme.business.domain;

import nu.hjemme.business.domain.base.PersistentEntity;
import nu.hjemme.business.domain.persistence.ProfileEntity;
import nu.hjemme.client.datatype.Name;
import nu.hjemme.client.domain.Address;

/** @author Tor Egil Jacobsen */
public class Profile extends PersistentEntity implements nu.hjemme.client.domain.Profile {
    private final ProfileEntity profileEntity;

    public Profile(ProfileEntity profileEntity) {
        this.profileEntity = profileEntity;
    }

    @Override
    public String getDescription() {
        return profileEntity.getDescription();
    }

    @Override
    public User getUser() {
        return new User(profileEntity.getUser());
    }

    @Override
    public Name getFirstName() {
        return profileEntity.getFirstName();
    }

    @Override
    public Name getLastName() {
        return profileEntity.getLastName();
    }

    @Override
    public Address getAddress() {
        return profileEntity.getAddress();
    }
}
