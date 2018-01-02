package com.github.jactorrises.client.persistence.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

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
        addressDto.setCountry("NO");
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

    @DisplayName("should give values to PersistentDto")
    @Test
    void shouldGiveValuesToPersistentDto() {
        AddressDto persistentDto = new AddressDto();
        persistentDto.setCreatedBy("jactor");
        persistentDto.setCreationTime(LocalDateTime.now());
        persistentDto.setId(1L);
        persistentDto.setUpdatedBy("tip");
        persistentDto.setUpdatedTime(LocalDateTime.now());

        PersistentDto copied = new AddressDto(persistentDto);

        assertAll(
                () -> Assertions.assertThat(copied.getCreatedBy()).as("created by").isEqualTo(persistentDto.getCreatedBy()),
                () -> Assertions.assertThat(copied.getCreationTime()).as("creation time").isEqualTo(persistentDto.getCreationTime()),
                () -> Assertions.assertThat(copied.getId()).as("id").isEqualTo(persistentDto.getId()),
                () -> Assertions.assertThat(copied.getUpdatedBy()).as("updated by").isEqualTo(persistentDto.getUpdatedBy()),
                () -> Assertions.assertThat(copied.getUpdatedTime()).as("updated time").isEqualTo(persistentDto.getUpdatedTime())
        );
    }
}
