package com.gitlab.jactor.rises.model.domain.address;

import com.gitlab.jactor.rises.commons.datatype.Country;
import com.gitlab.jactor.rises.model.domain.Address;
import com.gitlab.jactor.rises.commons.dto.AddressDto;
import com.gitlab.jactor.rises.model.domain.PersistentDomain;

import java.util.Optional;

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
        return Optional.ofNullable(addressDto.getCountry()).map(Country::new).orElse(null);
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
