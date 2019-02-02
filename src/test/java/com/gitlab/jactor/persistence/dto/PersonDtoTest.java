package com.gitlab.jactor.persistence.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("A PersonDto")
class PersonDtoTest {

    @DisplayName("should have a copy constructor")
    @Test void shouldHaveCopyConstructor() {
        PersonDto personDto = new PersonDto();
        personDto.setAddress(new AddressDto());
        personDto.setDescription("description");
        personDto.setFirstName("first name");
        personDto.setLocale("no");
        personDto.setSurname("surname");

        PersonDto copied = new PersonDto(
                personDto.asPersistentDto(), personDto.getAddress(), personDto.getLocale(),
                personDto.getFirstName(), personDto.getSurname(), personDto.getDescription()
        );

        assertAll(
                () -> assertThat(copied.getAddress()).as("address").isEqualTo(personDto.getAddress()),
                () -> assertThat(copied.getDescription()).as("description").isEqualTo(personDto.getDescription()),
                () -> assertThat(copied.getFirstName()).as("first name").isEqualTo(personDto.getFirstName()),
                () -> assertThat(copied.getLocale()).as("locale").isEqualTo(personDto.getLocale()),
                () -> assertThat(copied.getSurname()).as("surname").isEqualTo(personDto.getSurname())
        );
    }

    @DisplayName("should give values to PersistentDto")
    @Test void shouldGiveValuesToPersistentDto() {
        PersonDto personDto = new PersonDto();
        personDto.setCreatedBy("jactor");
        personDto.setCreationTime(LocalDateTime.now());
        personDto.setId(1L);
        personDto.setUpdatedBy("tip");
        personDto.setUpdatedTime(LocalDateTime.now());

        PersistentDto copied = new PersonDto(
                personDto.asPersistentDto(), null, null, null, null, null
        ).asPersistentDto();

        assertAll(
                () -> assertThat(copied.getCreatedBy()).as("created by").isEqualTo(personDto.getCreatedBy()),
                () -> assertThat(copied.getCreationTime()).as("creation time").isEqualTo(personDto.getCreationTime()),
                () -> assertThat(copied.getId()).as("id").isEqualTo(personDto.getId()),
                () -> assertThat(copied.getUpdatedBy()).as("updated by").isEqualTo(personDto.getUpdatedBy()),
                () -> assertThat(copied.getUpdatedTime()).as("updated time").isEqualTo(personDto.getUpdatedTime())
        );
    }
}
