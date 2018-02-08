package com.github.jactor.rises.model.domain.address;

import com.github.jactorrises.client.datatype.Country;
import com.github.jactorrises.client.domain.Address;
import com.github.jactorrises.client.dto.AddressDto;
import com.github.jactor.rises.model.domain.PersistentDomain;

public class AddressDomain extends PersistentDomain implements Address {

    private final AddressDto addressDto;

    public AddressDomain(AddressDto addressDto) {
        this.addressDto = addressDto;
    }

    @Override public String getAddressLine1() {
        return addressDto.getAddressLine1();
    }

    @Override public String getAddressLine2() {
        return addressDto.getAddressLine2();
    }

    @Override public String getAddressLine3() {
        return addressDto.getAddressLine3();
    }

    @Override public String getCity() {
        return addressDto.getCity();
    }

    @Override public Country getCountry() {
        return addressDto.getCountry() != null ? new Country(addressDto.getCountry()) : null;
    }

    @Override public Integer getZipCode() {
        return addressDto.getZipCode();
    }

    @Override public AddressDto getDto() {
        return addressDto;
    }

    public static AddressBuilder anAddress() {
        return new AddressBuilder();
    }
}
