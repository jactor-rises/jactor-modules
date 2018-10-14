package com.gitlab.jactor.rises.model.domain.address;

import com.gitlab.jactor.rises.commons.datatype.Country;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.gitlab.jactor.rises.model.domain.address.AddressDomain.anAddress;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("The AddressDomainBuilder")
class AddressBuilderTest {

    @DisplayName("should not build an instance without an address line 1")
    @Test void willNotBuildDomainWithoutAddressLine1() {
        assertThatIllegalStateException().isThrownBy(() -> anAddress().withZipCode(1234).withCountry("NO").build())
                .withMessageContaining("addressLine1");
    }

    @DisplayName("should not build an instance with an empty address line 1")
    @Test void willNotBuildDomainWithAnEmptyAddressLine1() {
        assertThatIllegalStateException().isThrownBy(() -> anAddress().withAddressLine1("").withZipCode(1234).withCountry("NO").build())
                .withMessageContaining("addressLine1");
    }

    @DisplayName("should not build an instance without a zip code")
    @Test void willNotBuildDomainWithoutZipCode() {
        assertThatIllegalStateException().isThrownBy(() -> anAddress().withAddressLine1("somewhere").withCountry("NO").build())
                .withMessageContaining("zipCode");
    }

    @DisplayName("should not build an instance without a country")
    @Test void willNotBuildDomainWithoutCountry() {
        assertThatIllegalStateException().isThrownBy(() -> anAddress().withAddressLine1("somewhere").withZipCode(1234).build())
                .withMessageContaining("country");
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

    @DisplayName("should build an instance where all properties are expected")
    @Test void whenBuildingAnAddressAllAddressLinesAndItsCityCanAlsoBeAppended() {
        AddressDomain addressDomain = anAddress()
                .withAddressLine1("somewhere")
                .withAddressLine2("somewhere else")
                .withAddressLine3("way out there")
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
