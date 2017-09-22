package com.github.jactorrises.model.domain.address;

import com.github.jactorrises.model.Builder;
import com.github.jactorrises.model.persistence.entity.address.AddressEntity;
import com.github.jactorrises.model.persistence.entity.address.AddressEntityBuilder;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

import static com.github.jactorrises.model.persistence.entity.address.AddressEntity.anAddress;
import static java.util.Arrays.asList;

public final class AddressBuilder extends Builder<AddressDomain> {
    static final String ADDRESS_LINE_1_CANNOT_BE_EMPTY = "Address line 1 cannot be empty";
    static final String COUNTRY_CANNOT_BE_NULL = "A country must be provided";
    static final String ZIP_CODE_CANNOT_BE_NULL = "A Zip code must be provided";

    private final AddressEntityBuilder addressEntityBuilder = anAddress();

    AddressBuilder() {
        super(asList(
                domain -> StringUtils.isNotBlank(domain.getAddressLine1()) ? Optional.empty() : Optional.of(ADDRESS_LINE_1_CANNOT_BE_EMPTY),
                domain -> domain.getZipCode() != null ? Optional.empty() : Optional.of(ZIP_CODE_CANNOT_BE_NULL),
                domain -> domain.getCountry() != null ? Optional.empty() : Optional.of(COUNTRY_CANNOT_BE_NULL)
        ));
    }

    public AddressBuilder withCity(String city) {
        addressEntityBuilder.withCity(city);
        return this;
    }

    public AddressBuilder withCountry(String country) {
        addressEntityBuilder.withCountryCode(country);
        return this;
    }

    public AddressBuilder withAddressLine1(String addressLine1) {
        addressEntityBuilder.withAddressLine1(addressLine1);
        return this;
    }

    AddressBuilder appendAddressLine2(String addressLine2) {
        addressEntityBuilder.withAddressLine2(addressLine2);
        return this;
    }

    AddressBuilder appendAddressLine3(String addressLine3) {
        addressEntityBuilder.withAddressLine3(addressLine3);
        return this;
    }

    public AddressBuilder withZipCode(Integer zipCode) {
        addressEntityBuilder.withZipCode(zipCode);
        return this;
    }

    @Override protected AddressDomain buildBean() {
        return new AddressDomain(addressEntityBuilder.build());
    }

    public static AddressDomain build(AddressEntity addressEntity) {
        return new AddressDomain(addressEntity);
    }
}
