package com.github.jactor.persistence.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("A PersonDto")
class PersonInternalDtoTest {

    @DisplayName("should have a copy constructor")
    @Test void shouldHaveCopyConstructor() {
        PersonInternalDto personInternalDto = new PersonInternalDto();
        personInternalDto.setAddress(new AddressInternalDto());
        personInternalDto.setDescription("description");
        personInternalDto.setFirstName("first name");
        personInternalDto.setLocale("no");
        personInternalDto.setSurname("surname");

        PersonInternalDto copied = new PersonInternalDto(personInternalDto.getPersistentDto(), personInternalDto);

        assertAll(
                () -> assertThat(copied.getAddress()).as("address").isEqualTo(personInternalDto.getAddress()),
                () -> assertThat(copied.getDescription()).as("description").isEqualTo(personInternalDto.getDescription()),
                () -> assertThat(copied.getFirstName()).as("first name").isEqualTo(personInternalDto.getFirstName()),
                () -> assertThat(copied.getLocale()).as("locale").isEqualTo(personInternalDto.getLocale()),
                () -> assertThat(copied.getSurname()).as("surname").isEqualTo(personInternalDto.getSurname())
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

        PersistentDto copied = new PersonInternalDto(persistentDto, new PersonInternalDto()).getPersistentDto();

        assertAll(
                () -> assertThat(copied.getCreatedBy()).as("created by").isEqualTo(persistentDto.getCreatedBy()),
                () -> assertThat(copied.getTimeOfCreation()).as("creation time").isEqualTo(persistentDto.getTimeOfCreation()),
                () -> assertThat(copied.getId()).as("id").isEqualTo(persistentDto.getId()),
                () -> assertThat(copied.getModifiedBy()).as("updated by").isEqualTo(persistentDto.getModifiedBy()),
                () -> assertThat(copied.getTimeOfModification()).as("updated time").isEqualTo(persistentDto.getTimeOfModification())
        );
    }
}
