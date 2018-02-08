package com.github.jactor.rises.model.domain.address;

import com.github.jactorrises.client.dto.AddressDto;
import com.github.jactor.rises.commons.builder.AbstractBuilder;

import java.util.Optional;

import static com.github.jactor.rises.commons.builder.ValidInstance.collectMessages;
import static com.github.jactor.rises.commons.builder.ValidInstance.fetchMessageIfFieldNotPresent;
import static com.github.jactor.rises.commons.builder.ValidInstance.fetchMessageIfStringWithoutValue;

public final class AddressBuilder extends AbstractBuilder<AddressDomain> {
    private final AddressDto addressDto = new AddressDto();

    AddressBuilder() {
        super(AddressBuilder::validInstance);
    }

    public AddressBuilder withCity(String city) {
        addressDto.setCity(city);
        return this;
    }

    public AddressBuilder withCountry(String country) {
        addressDto.setCountry(country);
        return this;
    }

    public AddressBuilder withAddressLine1(String addressLine1) {
        addressDto.setAddressLine1(addressLine1);
        return this;
    }

    AddressBuilder appendAddressLine2(@SuppressWarnings("SameParameterValue") String addressLine2) {
        addressDto.setAddressLine2(addressLine2);
        return this;
    }

    AddressBuilder appendAddressLine3(@SuppressWarnings("SameParameterValue") String addressLine3) {
        addressDto.setAddressLine3(addressLine3);
        return this;
    }

    public AddressBuilder withZipCode(Integer zipCode) {
        addressDto.setZipCode(zipCode);
        return this;
    }

    @Override protected AddressDomain buildBean() {
        return new AddressDomain(addressDto);
    }

    private static Optional<String> validInstance(AddressDomain addressDomain) {
        return collectMessages(
                fetchMessageIfStringWithoutValue("address line 1", addressDomain.getAddressLine1()),
                fetchMessageIfFieldNotPresent("country", addressDomain.getCountry()),
                fetchMessageIfFieldNotPresent("zip code", addressDomain.getZipCode())
        );
    }

    public static AddressDomain build(AddressDto addressDto) {
        return new AddressDomain(addressDto);
    }
}
