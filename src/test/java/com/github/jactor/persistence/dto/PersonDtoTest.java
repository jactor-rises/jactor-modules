package com.github.jactor.persistence.dto;

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

        PersonDto copied = new PersonDto(personDto.fetchPersistentDto(), personDto);

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
        PersistentDto persistentDto = new PersistentDto();
        persistentDto.setCreatedBy("jactor");
        persistentDto.setTimeOfCreation(LocalDateTime.now());
        persistentDto.setId(1L);
        persistentDto.setModifiedBy("tip");
        persistentDto.setTimeOfModification(LocalDateTime.now());

        PersistentDto copied = new PersonDto(persistentDto, new PersonDto()).fetchPersistentDto();

        assertAll(
                () -> assertThat(copied.getCreatedBy()).as("created by").isEqualTo(persistentDto.getCreatedBy()),
                () -> assertThat(copied.getTimeOfCreation()).as("creation time").isEqualTo(persistentDto.getTimeOfCreation()),
                () -> assertThat(copied.getId()).as("id").isEqualTo(persistentDto.getId()),
                () -> assertThat(copied.getModifiedBy()).as("updated by").isEqualTo(persistentDto.getModifiedBy()),
                () -> assertThat(copied.getTimeOfModification()).as("updated time").isEqualTo(persistentDto.getTimeOfModification())
        );
    }
}
