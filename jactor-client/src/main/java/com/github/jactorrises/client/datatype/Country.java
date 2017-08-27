package com.github.jactorrises.client.datatype;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Locale;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.Validate.notEmpty;

/**
 * Representing a country according to the ISO 3166 standard. In addition, the country code will together with the language code represent specific {@link Locale}.
 */
public class Country {
    private static final String VALID_COUNTRY_CODES = ", valid codes: ";
    static final String NOT_A_VALID_COUNTRY_CODE_ACCORDING_TO_ISO_3166 = " is not a valid countryCode according to ISO 3166";
    static final String THE_COUNTRY_CODE_CANNOT_BE_EMPTY = "The country code cannot be empty";
    private String countryCode;

    public Country(String countryCode) {
        this.countryCode = countryCode;
        validate(countryCode);
    }

    private void validate(String countryCode) {
        notEmpty(countryCode, THE_COUNTRY_CODE_CANNOT_BE_EMPTY);

        String[] isoCountries = Locale.getISOCountries();

        for (String country : isoCountries) {
            if (countryCode.equals(country)) {
                return;
            }
        }

        throw new IllegalArgumentException(countryCode + NOT_A_VALID_COUNTRY_CODE_ACCORDING_TO_ISO_3166 + VALID_COUNTRY_CODES + asList(isoCountries));
    }

    @Override public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass() && ((Country) obj).getCountryCode().equals(countryCode);
    }

    @Override public int hashCode() {
        return countryCode.hashCode();
    }

    @Override public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append(countryCode)
                .toString();
    }

    public String getCountryCode() {
        return countryCode;
    }
}
