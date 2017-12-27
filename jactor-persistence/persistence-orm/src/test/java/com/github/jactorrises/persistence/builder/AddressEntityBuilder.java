package com.github.jactorrises.persistence.builder;

import com.github.jactorrises.client.datatype.Country;
import com.github.jactorrises.persistence.entity.address.AddressOrm;

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

    public AddressOrm build() {
        AddressOrm addressOrm = new AddressOrm();
        addressOrm.setAddressLine1(addressLine1);
        addressOrm.setAddressLine2(addressLine2);
        addressOrm.setAddressLine3(addressLine3);
        addressOrm.setZipCode(zipCode);
        addressOrm.setCity(city);
        addressOrm.setCountry(country);

        return addressOrm;
    }

    public static AddressEntityBuilder anAddress() {
        return new AddressEntityBuilder();
    }
}
