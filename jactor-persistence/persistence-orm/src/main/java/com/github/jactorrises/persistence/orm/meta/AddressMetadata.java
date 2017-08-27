package com.github.jactorrises.persistence.orm.meta;

public final class AddressMetadata {
    private AddressMetadata() {}

    public static final String TABLE = "T_ADDRESS";

    /** The country where the address is located */
    public static final String COUNTRY = "COUNTRY";
    /** The zip code of the address */
    public static final String ZIP_CODE = "ZIP_CODE";
    /** Address line 1 of the address */
    public static final String ADDRESS_LINE_1 = "ADDRESS_LINE_1";
    /** Address line 2 of the address */
    public static final String ADDRESS_LINE_2 = "ADDRESS_LINE_2";
    /** Address line 3 of the address */
    public static final String ADDRESS_LINE_3 = "ADDRESS_LINE_3";
    /** The city where the address is located */
    public static final String CITY = "CITY";
}
