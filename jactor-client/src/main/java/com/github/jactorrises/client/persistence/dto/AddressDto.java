package com.github.jactorrises.client.persistence.dto;

import com.github.jactorrises.client.datatype.Country;
import com.github.jactorrises.client.domain.Address;

public class AddressDto extends PersistentDto implements Address {
    private Country country;
    private Integer zipCode;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String city;

    public AddressDto() {
        // empty, use setters
    }

    AddressDto(AddressDto addressDto) {
        super(addressDto);
        this.country = addressDto.getCountry();
        this.zipCode = addressDto.getZipCode();
        this.addressLine1 = addressDto.getAddressLine1();
        this.addressLine2 = addressDto.getAddressLine2();
        this.addressLine3 = addressDto.getAddressLine3();
        this.city = addressDto.getCity();
    }

    @Override public String getAddressLine1() {
        return addressLine1;
    }

    @Override public String getAddressLine2() {
        return addressLine2;
    }

    @Override public String getAddressLine3() {
        return addressLine3;
    }

    @Override public String getCity() {
        return city;
    }

    @Override public Country getCountry() {
        return country;
    }

    @Override public Integer getZipCode() {
        return zipCode;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public void setAddressLine3(String addressLine3) {
        this.addressLine3 = addressLine3;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setZipCode(Integer zipCode) {
        this.zipCode = zipCode;
    }
}
