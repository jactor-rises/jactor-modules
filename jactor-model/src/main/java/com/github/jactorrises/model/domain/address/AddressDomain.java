package com.github.jactorrises.model.domain.address;

import com.github.jactorrises.client.datatype.Country;
import com.github.jactorrises.client.domain.Address;
import com.github.jactorrises.model.domain.PersistentDomain;
import com.github.jactorrises.model.persistence.entity.address.AddressEntity;

public class AddressDomain extends PersistentDomain<AddressEntity, Long> implements Address {

    private final AddressEntity addressEntity;

    public AddressDomain(AddressEntity addressEntity) {
        this.addressEntity = addressEntity;
    }

    @Override public String getAddressLine1() {
        return addressEntity.getAddressLine1();
    }

    @Override public String getAddressLine2() {
        return addressEntity.getAddressLine2();
    }

    @Override public String getAddressLine3() {
        return addressEntity.getAddressLine3();
    }

    @Override public String getCity() {
        return addressEntity.getCity();
    }

    @Override public Country getCountry() {
        return addressEntity.getCountry();
    }

    @Override public Integer getZipCode() {
        return addressEntity.getZipCode();
    }

    @Override public AddressEntity getEntity() {
        return addressEntity;
    }

    public static AddressBuilder anAddress() {
        return new AddressBuilder();
    }
}
