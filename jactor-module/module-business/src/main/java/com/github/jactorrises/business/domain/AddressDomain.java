package com.github.jactorrises.business.domain;

import com.github.jactorrises.business.domain.builder.AddressDomainBuilder;
import com.github.jactorrises.client.datatype.Country;
import com.github.jactorrises.client.domain.Address;
import com.github.jactorrises.persistence.entity.address.AddressEntity;

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

    public static AddressDomainBuilder anAddress() {
        return AddressDomainBuilder.init();
    }
}
