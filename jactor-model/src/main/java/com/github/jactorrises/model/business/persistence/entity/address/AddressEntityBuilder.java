package com.github.jactorrises.model.business.persistence.entity.address;

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

    public AddressEntity build() {
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setAddressLine1(addressLine1);
        addressEntity.setZipCode(zipCode);
        addressEntity.setCity(city);

        return addressEntity;
    }
}
