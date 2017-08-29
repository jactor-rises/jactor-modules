package com.github.jactorrises.persistence.boot.entity.address;

public class AddressEntityBuilder {

    private String addressLine1;
    private String city;
    private int zipCode;

    AddressEntityBuilder() {
    }

    public AddressEntityBuilder withAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
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

    public AddressEntityImpl build() {
        AddressEntityImpl addressEntity = new AddressEntityImpl();
        addressEntity.setAddressLine1(addressLine1);
        addressEntity.setZipCode(zipCode);
        addressEntity.setCity(city);

        return addressEntity;
    }
}
