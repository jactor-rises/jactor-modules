package com.github.jactor.web.dto;

import com.github.jactor.shared.dto.AddressDto;

public class AddressDtoBuilder {

  private String addressLine1;
  private String addressLine2;
  private String addressLine3;
  private String city;
  private String zipCode;

  public AddressDtoBuilder withAddressLine1(String addressLine1) {
    this.addressLine1 = addressLine1;
    return this;
  }

  public AddressDtoBuilder withAddressLine2(String addressLine2) {
    this.addressLine2 = addressLine2;
    return this;
  }

  public AddressDtoBuilder withAddressLine3(String addressLine3) {
    this.addressLine3 = addressLine3;
    return this;
  }

  public AddressDtoBuilder withZipCode(String zipCode) {
    this.zipCode = zipCode;
    return this;
  }

  public AddressDtoBuilder withCity(String city) {
    this.city = city;
    return this;
  }

  AddressDto build() {
    AddressDto addressDto = new AddressDto();
    addressDto.setAddressLine1(addressLine1);
    addressDto.setAddressLine2(addressLine2);
    addressDto.setAddressLine3(addressLine3);
    addressDto.setCity(city);
    addressDto.setZipCode(zipCode);

    return addressDto;
  }
}
