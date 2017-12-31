package com.github.jactorrises.persistence.builder;

import com.github.jactorrises.client.datatype.Country;
import com.github.jactorrises.persistence.entity.address.AddressEntity;

public class AddressEntityBuilder {

    private Country country;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String city;
    private Integer zipCode;

    private AddressEntityBuilder() {
    }

    public AddressEntityBuilder withAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
        return this;
    }

    public AddressEntityBuilder withAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
        return this;
    }

    public AddressEntityBuilder withAddressLine3(String addressLine3) {
        this.addressLine3 = addressLine3;
        return this;
    }

    public AddressEntityBuilder withCountryCode(String countryCode) {
        country = new Country(countryCode);
        return this;
    }

    public AddressEntityBuilder withZipCode(int zipCode) {
        this.zipCode = zipCode;
        return this;
    }

    public AddressEntityBuilder withCity(String city) {
        this.city = city;
        return this;
    }

    public AddressEntity build() {
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setAddressLine1(addressLine1);
        addressEntity.setAddressLine2(addressLine2);
        addressEntity.setAddressLine3(addressLine3);
        addressEntity.setZipCode(zipCode);
        addressEntity.setCity(city);
        addressEntity.setCountry(country);

        return addressEntity;
    }

    public static AddressEntityBuilder anAddress() {
        return new AddressEntityBuilder();
    }
}
