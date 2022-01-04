package com.github.jactor.persistence.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("An AddressDto")
class AddressInternalDtoTest {

    @DisplayName("should have a copy constructor")
    @Test void shouldHaveCopyConstructor() {
        AddressInternalDto addressInternalDto = new AddressInternalDto();
        addressInternalDto.setAddressLine1("address line one");
        addressInternalDto.setAddressLine2("address line two");
        addressInternalDto.setAddressLine3("address line three");
        addressInternalDto.setCity("oslo");
        addressInternalDto.setCountry("NO");
        addressInternalDto.setZipCode("1234");

        AddressInternalDto copied = new AddressInternalDto(addressInternalDto.getPersistentDto(), addressInternalDto);

        assertAll(
                () -> assertThat(copied.getAddressLine1()).as("address line one").isEqualTo(addressInternalDto.getAddressLine1()),
                () -> assertThat(copied.getAddressLine2()).as("address line two").isEqualTo(addressInternalDto.getAddressLine2()),
                () -> assertThat(copied.getAddressLine3()).as("address line three").isEqualTo(addressInternalDto.getAddressLine3()),
                () -> assertThat(copied.getCity()).as("city").isEqualTo(addressInternalDto.getCity()),
                () -> assertThat(copied.getCountry()).as("country").isEqualTo(addressInternalDto.getCountry()),
                () -> assertThat(copied.getZipCode()).as("zip code").isEqualTo(addressInternalDto.getZipCode())
        );
    }

    @DisplayName("should give values to PersistentDto")
    @Test void shouldGiveValuesToPersistentDto() {
        PersistentDto persistentDto = new PersistentDto();
        persistentDto.setCreatedBy("jactor");
        persistentDto.setTimeOfCreation(LocalDateTime.now());
        persistentDto.setId(1L);
        persistentDto.setModifiedBy("tip");
        persistentDto.setTimeOfModification(LocalDateTime.now());

        PersistentDto copied = new AddressInternalDto(persistentDto, new AddressInternalDto()).getPersistentDto();

        assertAll(
                () -> Assertions.assertThat(copied.getCreatedBy()).as("created by").isEqualTo(persistentDto.getCreatedBy()),
                () -> Assertions.assertThat(copied.getTimeOfCreation()).as("creation time").isEqualTo(persistentDto.getTimeOfCreation()),
                () -> Assertions.assertThat(copied.getId()).as("id").isEqualTo(persistentDto.getId()),
                () -> Assertions.assertThat(copied.getModifiedBy()).as("updated by").isEqualTo(persistentDto.getModifiedBy()),
                () -> Assertions.assertThat(copied.getTimeOfModification()).as("updated time").isEqualTo(persistentDto.getTimeOfModification())
        );
    }
}
