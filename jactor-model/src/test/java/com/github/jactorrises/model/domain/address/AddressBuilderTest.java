package com.github.jactorrises.model.domain.address;

import com.github.jactorrises.client.datatype.Country;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.jactorrises.model.domain.address.AddressDomain.anAddress;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("The AdressDomainBuilder")
class AddressBuilderTest {

    @DisplayName("should not build an instance without an address line 1")
    @Test void willNotBuildDomainWithoutAddressLine1() {
        assertThatIllegalArgumentException().isThrownBy(() -> anAddress().withZipCode(1234).withCountry("NO").build())
                .withMessage(AddressBuilder.ADDRESS_LINE_1_CANNOT_BE_EMPTY);
    }

    @DisplayName("should not build an instance with an empty address line 1")
    @Test void willNotBuildDomainWithAnEmptyAddressLine1() {
        assertThatIllegalArgumentException().isThrownBy(() -> anAddress().withAddressLine1("").withZipCode(1234).withCountry("NO").build())
                .withMessage(AddressBuilder.ADDRESS_LINE_1_CANNOT_BE_EMPTY);
    }

    @DisplayName("should not build an instance without a zip code")
    @Test void willNotBuildDomainWithoutZipCode() {
        assertThatIllegalArgumentException().isThrownBy(() -> anAddress().withAddressLine1("somewhere").withCountry("NO").build())
                .withMessage(AddressBuilder.ZIP_CODE_CANNOT_BE_NULL);
    }

    @DisplayName("should not build an instance without a country")
    @Test void willNotBuildDomainWithoutCountry() {
        assertThatIllegalArgumentException().isThrownBy(() -> anAddress().withAddressLine1("somewhere").withZipCode(1234).build())
                .withMessage(AddressBuilder.COUNTRY_CANNOT_BE_NULL);
    }

    @DisplayName("should build an instance when all required properties are set")
    @Test void willBuildValidatedDomain() {
        AddressDomain addressDomain = anAddress()
                .withAddressLine1("somewhere")
                .withZipCode(1234)
                .withCountry("NO")
                .build();

        assertThat(addressDomain).isNotNull();
    }

    @DisplayName("should build an instance with all properties set")
    @Test void whenBuildingAnAddressAllAddressLinesAndItsCityCanAlsoBeAppended() {
        AddressDomain addressDomain = anAddress()
                .withAddressLine1("somewhere")
                .appendAddressLine2("somewhere else")
                .appendAddressLine3("way out there")
                .withCity("some city")
                .withCountry("NO")
                .withZipCode(1234)
                .build();

        assertAll("A domain with all properties set",
                () -> assertThat(addressDomain.getAddressLine1()).as("address line 1").isEqualTo("somewhere"),
                () -> assertThat(addressDomain.getAddressLine2()).as("address line 2").isEqualTo("somewhere else"),
                () -> assertThat(addressDomain.getAddressLine3()).as("address line 3").isEqualTo("way out there"),
                () -> assertThat(addressDomain.getCity()).as("city").isEqualTo("some city"),
                () -> assertThat(addressDomain.getCountry()).as("country").isEqualTo(new Country("NO")),
                () -> assertThat(addressDomain.getZipCode()).as("zip code").isEqualTo(1234)
        );
    }
}
