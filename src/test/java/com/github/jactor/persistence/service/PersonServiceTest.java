package com.github.jactor.persistence.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.jactor.persistence.JactorPersistence;
import com.github.jactor.persistence.dto.PersistentDto;
import com.github.jactor.persistence.dto.PersonInternalDto;
import com.github.jactor.persistence.entity.PersonEntity;
import com.github.jactor.persistence.repository.PersonRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@DisplayName("A PersonService")
@SpringBootTest(classes = JactorPersistence.class)
class PersonServiceTest {

  @Autowired
  private PersonService personService;
  @MockBean
  private PersonRepository personRepositoryMock;

  @Test
  @DisplayName("should create a new Person")
  void shouldCreateNewUser() {
    personService.createWhenNotExists(new PersonInternalDto());

    ArgumentCaptor<PersonEntity> personEntityCaptor = ArgumentCaptor.forClass(PersonEntity.class);
    verify(personRepositoryMock).save(personEntityCaptor.capture());

    assertThat(personEntityCaptor.getValue()).isNotNull();
  }

  @Test
  @DisplayName("should find Person by id")
  void shouldFiedPersonById() {
    // given
    var personEntity = new PersonEntity(new PersonInternalDto());

    when(personRepositoryMock.findById(1L)).thenReturn(Optional.of(personEntity));

    // when
    var person = personService.createWhenNotExists(
        new PersonInternalDto(new PersistentDto(1L, "creator", LocalDateTime.now(), "modifier", LocalDateTime.now()), new PersonInternalDto())
    );

    // then
    assertAll(
        () -> verify(personRepositoryMock, never()).save(any()),
        () -> assertThat(person).as("person").isEqualTo(personEntity)
    );
  }
}