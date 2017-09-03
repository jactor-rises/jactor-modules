package com.github.jactorrises.persistence.entity.address;

final class AddressMetadata {
    private AddressMetadata() {
    }

    static final String ADDRESS = "T_ADDRESS";

    /**
     * The country where the address is located
     */
    static final String COUNTRY = "COUNTRY";
    /**
     * The zip code of the address
     */
    static final String ZIP_CODE = "ZIP_CODE";
    /**
     * Address line 1 of the address
     */
    static final String ADDRESS_LINE_1 = "ADDRESS_LINE_1";
    /**
     * Address line 2 of the address
     */
    static final String ADDRESS_LINE_2 = "ADDRESS_LINE_2";
    /**
     * Address line 3 of the address
     */
    static final String ADDRESS_LINE_3 = "ADDRESS_LINE_3";
    /**
     * The city where the address is located
     */
    static final String CITY = "CITY";
}
