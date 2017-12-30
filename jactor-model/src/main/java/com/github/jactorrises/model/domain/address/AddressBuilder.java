package com.github.jactorrises.model.domain.address;

import com.github.jactorrises.commons.builder.AbstractBuilder;
import com.github.jactorrises.persistence.builder.AddressEntityBuilder;
import com.github.jactorrises.persistence.client.entity.AddressEntity;

import java.util.Optional;

import static com.github.jactorrises.commons.builder.ValidInstance.collectMessages;
import static com.github.jactorrises.commons.builder.ValidInstance.fetchMessageIfFieldNotPresent;
import static com.github.jactorrises.commons.builder.ValidInstance.fetchMessageIfStringWithoutValue;

public final class AddressBuilder extends AbstractBuilder<AddressDomain> {
    private final AddressEntityBuilder addressEntityBuilder = AddressEntityBuilder.anAddress();

    AddressBuilder() {
        super(AddressBuilder::validInstance);
    }

    public AddressBuilder withCity(String city) {
        addressEntityBuilder.withCity(city);
        return this;
    }

    public AddressBuilder withCountry(String country) {
        addressEntityBuilder.withCountryCode(country);
        return this;
    }

    public AddressBuilder withAddressLine1(String addressLine1) {
        addressEntityBuilder.withAddressLine1(addressLine1);
        return this;
    }

    AddressBuilder appendAddressLine2(String addressLine2) {
        addressEntityBuilder.withAddressLine2(addressLine2);
        return this;
    }

    AddressBuilder appendAddressLine3(String addressLine3) {
        addressEntityBuilder.withAddressLine3(addressLine3);
        return this;
    }

    public AddressBuilder withZipCode(Integer zipCode) {
        addressEntityBuilder.withZipCode(zipCode);
        return this;
    }

    @Override protected AddressDomain buildBean() {
        return new AddressDomain(addressEntityBuilder.build());
    }

    private static Optional<String> validInstance(AddressDomain addressDomain) {
        return collectMessages(
                fetchMessageIfStringWithoutValue("address line 1", addressDomain.getAddressLine1()),
                fetchMessageIfFieldNotPresent("country", addressDomain.getCountry()),
                fetchMessageIfFieldNotPresent("zip code", addressDomain.getZipCode())
        );
    }

    public static AddressDomain build(AddressEntity addressEntity) {
        return new AddressDomain(addressEntity);
    }
}
