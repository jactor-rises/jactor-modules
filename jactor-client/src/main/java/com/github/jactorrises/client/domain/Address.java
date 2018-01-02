package com.github.jactorrises.client.domain;

import com.github.jactorrises.client.datatype.Country;

public interface Address extends Persistent {

    String getAddressLine1();

    String getAddressLine2();

    String getAddressLine3();

    String getCity();

    Country getCountry();

    Integer getZipCode();
}
