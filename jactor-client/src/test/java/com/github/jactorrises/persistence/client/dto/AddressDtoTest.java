package com.github.jactorrises.persistence.client.dto;

import com.github.jactorrises.client.datatype.Country;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("An AddressDto")
class AddressDtoTest {

    @DisplayName("should have a copy constructor")
    @Test
    void shouldHaveCopyConstructor() {
        AddressDto addressDto = new AddressDto();
        addressDto.setAddressLine1("address line one");
        addressDto.setAddressLine2("address line two");
        addressDto.setAddressLine3("address line three");
        addressDto.setCity("oslo");
        addressDto.setCountry(new Country("NO"));
        addressDto.setZipCode(1234);

        AddressDto copied = new AddressDto(addressDto);

        assertAll(
                () -> assertThat(copied.getAddressLine1()).as("address line one").isEqualTo(addressDto.getAddressLine1()),
                () -> assertThat(copied.getAddressLine2()).as("address line two").isEqualTo(addressDto.getAddressLine2()),
                () -> assertThat(copied.getAddressLine3()).as("address line three").isEqualTo(addressDto.getAddressLine3()),
                () -> assertThat(copied.getCity()).as("city").isEqualTo(addressDto.getCity()),
                () -> assertThat(copied.getCountry()).as("country").isEqualTo(addressDto.getCountry()),
                () -> assertThat(copied.getZipCode()).as("zip code").isEqualTo(addressDto.getZipCode())
        );
    }
}