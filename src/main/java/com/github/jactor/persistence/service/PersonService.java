package com.github.jactor.persistence.service;

import com.github.jactor.persistence.dto.PersonInternalDto;
import com.github.jactor.persistence.entity.PersonEntity;
import com.github.jactor.persistence.repository.PersonRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

  private final PersonRepository personRepository;

  public PersonService(PersonRepository personRepository) {
    this.personRepository = personRepository;
  }

  PersonEntity createWhenNotExists(PersonInternalDto person) {
    var personFound = findExisting(person);

    return personFound
        .orElseGet(() -> create(person));
  }

  private PersonEntity create(PersonInternalDto person) {
    return personRepository.save(new PersonEntity(person));
  }

  private Optional<PersonEntity> findExisting(PersonInternalDto person) {
    return Optional.ofNullable(person.getId())
        .flatMap(personRepository::findById);
  }
}
