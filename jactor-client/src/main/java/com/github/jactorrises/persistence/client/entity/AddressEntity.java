package com.github.jactorrises.persistence.client.entity;

import com.github.jactorrises.client.datatype.Country;
import com.github.jactorrises.client.domain.Address;
import com.github.jactorrises.client.domain.Persistent;

public interface AddressEntity extends Address, Persistent<Long> {

    void setCountry(Country country);

    void setZipCode(Integer zipCode);

    void setAddressLine1(String addressLine1);

    void setAddressLine2(String addressLine2);

    void setAddressLine3(String addressLine3);

    void setCity(String city);
}
