package com.github.jactorrises.model.business.domain.builder;

import com.github.jactorrises.model.business.domain.AddressDomain;
import com.github.jactorrises.model.business.persistence.entity.address.AddressEntity;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

import static java.util.Arrays.asList;

public final class AddressDomainBuilder extends DomainBuilder<AddressDomain> {
    static final String ADDRESS_LINE_1_CANNOT_BE_EMPTY = "Address line 1 cannot be empty";
    static final String COUNTRY_CANNOT_BE_NULL = "A country must be provided";
    static final String ZIP_CODE_CANNOT_BE_NULL = "A Zip code must be provided";

    private final AddressEntity addressEntity = new AddressEntity();

    private AddressDomainBuilder() {
        super(asList(
                domain -> StringUtils.isNotBlank(domain.getAddressLine1()) ? Optional.empty() : Optional.of(ADDRESS_LINE_1_CANNOT_BE_EMPTY),
                domain -> domain.getZipCode() != null ? Optional.empty() : Optional.of(ZIP_CODE_CANNOT_BE_NULL),
                domain -> domain.getCountry() != null ? Optional.empty() : Optional.of(COUNTRY_CANNOT_BE_NULL)
        ));
    }

    public AddressDomainBuilder withCityAs(String city) {
        addressEntity.setCity(city);
        return this;
    }

    public AddressDomainBuilder withCountryAs(String country) {
        addressEntity.setCountry(country);
        return this;
    }

    public AddressDomainBuilder withAddressLine1As(String addressLine1) {
        addressEntity.setAddressLine1(addressLine1);
        return this;
    }

    public AddressDomainBuilder appendAddressLine2(String addressLine2) {
        addressEntity.setAddressLine2(addressLine2);
        return this;
    }

    public AddressDomainBuilder appendAddressLine3(String addressLine3) {
        addressEntity.setAddressLine3(addressLine3);
        return this;
    }

    public AddressDomainBuilder withZipCodeAs(Integer zipCode) {
        addressEntity.setZipCode(zipCode);
        return this;
    }

    @Override protected AddressDomain addhRequiredFields() {
        return new AddressDomain(addressEntity);
    }

    public static AddressDomainBuilder init() {
        return new AddressDomainBuilder();
    }
}
