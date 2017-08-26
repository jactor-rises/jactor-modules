package nu.hjemme.client.domain;

import nu.hjemme.client.datatype.Country;

public interface Address extends Persistent<Long> {

    String getAddressLine1();

    String getAddressLine2();

    String getAddressLine3();

    String getCity();

    Country getCountry();

    Integer getZipCode();
}
