package com.github.jactorrises.model.domain.address;

import com.github.jactorrises.client.datatype.Country;
import com.github.jactorrises.model.Builder;
import com.github.jactorrises.model.persistence.entity.address.AddressEntity;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

import static java.util.Arrays.asList;

public final class AddressBuilder extends Builder<AddressDomain> {
    static final String ADDRESS_LINE_1_CANNOT_BE_EMPTY = "Address line 1 cannot be empty";
    static final String COUNTRY_CANNOT_BE_NULL = "A country must be provided";
    static final String ZIP_CODE_CANNOT_BE_NULL = "A Zip code must be provided";

    private final AddressEntity addressEntity = new AddressEntity();

    AddressBuilder() {
        super(asList(
                domain -> StringUtils.isNotBlank(domain.getAddressLine1()) ? Optional.empty() : Optional.of(ADDRESS_LINE_1_CANNOT_BE_EMPTY),
                domain -> domain.getZipCode() != null ? Optional.empty() : Optional.of(ZIP_CODE_CANNOT_BE_NULL),
                domain -> domain.getCountry() != null ? Optional.empty() : Optional.of(COUNTRY_CANNOT_BE_NULL)
        ));
    }

    public AddressBuilder withCity(String city) {
        addressEntity.setCity(city);
        return this;
    }

    public AddressBuilder withCountry(String country) {
        addressEntity.setCountry(new Country(country));
        return this;
    }

    public AddressBuilder withAddressLine1(String addressLine1) {
        addressEntity.setAddressLine1(addressLine1);
        return this;
    }

    AddressBuilder appendAddressLine2(String addressLine2) {
        addressEntity.setAddressLine2(addressLine2);
        return this;
    }

    AddressBuilder appendAddressLine3(String addressLine3) {
        addressEntity.setAddressLine3(addressLine3);
        return this;
    }

    public AddressBuilder withZipCode(Integer zipCode) {
        addressEntity.setZipCode(zipCode);
        return this;
    }

    @Override protected AddressDomain buildBeforeValidation() {
        return new AddressDomain(addressEntity);
    }

    public static AddressDomain build(AddressEntity addressEntity) {
        return new AddressDomain(addressEntity);
    }
}
