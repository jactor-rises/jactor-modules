package com.github.jactorrises.client.persistence.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("A PersonDto")
class PersonDtoTest {

    @DisplayName("should have a copy constructor")
    @Test
    void shouldHaveCopyConstructor() {
        PersonDto personDto = new PersonDto();
        personDto.setAddress(new AddressDto());
        personDto.setDescription("description");
        personDto.setFirstName("first name");
        personDto.setLocale("no");
        personDto.setSurname("surname");
        personDto.setUser(new UserDto());

        PersonDto copied = new PersonDto(personDto);

        assertAll(
                () -> assertThat(copied.getAddress()).as("address").isEqualTo(personDto.getAddress()),
                () -> assertThat(copied.getDescription()).as("description").isEqualTo(personDto.getDescription()),
                () -> assertThat(copied.getFirstName()).as("first name").isEqualTo(personDto.getFirstName()),
                () -> assertThat(copied.getLocale()).as("locale").isEqualTo(personDto.getLocale()),
                () -> assertThat(copied.getSurname()).as("surname").isEqualTo(personDto.getSurname()),
                () -> assertThat(copied.getUser()).as("user").isEqualTo(personDto.getUser())
        );
    }

    @DisplayName("should give values to PersistentDto")
    @Test
    void shouldGiveValuesToPersistentDto() {
        PersonDto persistentDto = new PersonDto();
        persistentDto.setCreatedBy("jactor");
        persistentDto.setCreationTime(LocalDateTime.now());
        persistentDto.setId(1L);
        persistentDto.setUpdatedBy("tip");
        persistentDto.setUpdatedTime(LocalDateTime.now());

        PersistentDto copied = new PersonDto(persistentDto);

        assertAll(
                () -> assertThat(copied.getCreatedBy()).as("created by").isEqualTo(persistentDto.getCreatedBy()),
                () -> assertThat(copied.getCreationTime()).as("creation time").isEqualTo(persistentDto.getCreationTime()),
                () -> assertThat(copied.getId()).as("id").isEqualTo(persistentDto.getId()),
                () -> assertThat(copied.getUpdatedBy()).as("updated by").isEqualTo(persistentDto.getUpdatedBy()),
                () -> assertThat(copied.getUpdatedTime()).as("updated time").isEqualTo(persistentDto.getUpdatedTime())
        );
    }
}
