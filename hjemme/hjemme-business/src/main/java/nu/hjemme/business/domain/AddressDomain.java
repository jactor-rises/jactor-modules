package nu.hjemme.business.domain;

import nu.hjemme.persistence.AddressEntity;
import nu.hjemme.client.datatype.Country;
import nu.hjemme.client.domain.Address;

/** @author Tor Egil Jacobsen */
public class AddressDomain extends PersistentDomain<AddressEntity, Long> implements Address {

    public AddressDomain(AddressEntity addressEntity) {
        super(addressEntity);
    }

    @Override
    public String getAddressLine1() {
        return getEntity().getAddressLine1();
    }

    @Override
    public String getAddressLine2() {
        return getEntity().getAddressLine2();
    }

    @Override
    public String getAddressLine3() {
        return getEntity().getAddressLine3();
    }

    @Override
    public String getCity() {
        return getEntity().getCity();
    }

    @Override
    public Country getCountry() {
        return getEntity().getCountry();
    }

    @Override
    public Integer getZipCode() {
        return getEntity().getZipCode();
    }
}