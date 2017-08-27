package com.github.jactorrises.persistence.client;

import com.github.jactorrises.client.domain.Address;

public interface AddressEntity extends Address {
    void setCity(String city);

    void setCountry(String country);

    void setAddressLine1(String addressLine1);

    void setAddressLine2(String addressLine2);

    void setAddressLine3(String addressLine3);

    void setZipCode(Integer zipCode);
}
