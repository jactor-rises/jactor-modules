package com.github.jactorrises.model.domain.address;

import com.github.jactorrises.commons.builder.AbstractBuilder;
import com.github.jactorrises.commons.builder.DomainValidator;
import com.github.jactorrises.persistence.builder.AddressEntityBuilder;
import com.github.jactorrises.persistence.client.entity.AddressEntity;
import org.apache.commons.lang3.StringUtils;

public final class AddressBuilder extends AbstractBuilder<AddressDomain> {
    private final AddressEntityBuilder addressEntityBuilder = AddressEntityBuilder.anAddress();

    AddressBuilder() {
        super(configureValidator());
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

    @Override protected AddressDomain buildDomain() {
        return new AddressDomain(addressEntityBuilder.build());
    }

    public static AddressDomain build(AddressEntity addressEntity) {
        return new AddressDomain(addressEntity);
    }

    private static DomainValidator<AddressDomain> configureValidator() {
        return new DomainValidator<AddressDomain>() {

            @Override public void validate(AddressDomain domain) {
                addIfInvalid(StringUtils.isBlank(domain.getAddressLine1()), "address line 1", FieldValidation.EMPTY);
                addIfInvalid(domain.getCountry() == null, "country", FieldValidation.REQUIRED);
                addIfInvalid(domain.getZipCode() == null, "zip code", FieldValidation.REQUIRED);
            }
        };
    }
}
