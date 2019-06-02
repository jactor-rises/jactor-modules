package com.github.jactor.persistence.service;

import com.github.jactor.persistence.dto.PersonDto;
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

  PersonEntity createWhenNotExists(PersonDto person) {
    var personFound = findExisting(person);

    return personFound
        .orElseGet(() -> create(person));
  }

  private PersonEntity create(PersonDto person) {
    return personRepository.save(new PersonEntity(person));
  }

  private Optional<PersonEntity> findExisting(PersonDto person) {
    var possibleExisting = Optional.ofNullable(person.getId())
        .map(personRepository::findById)
        .orElse(Optional.empty());

    return possibleExisting;
  }
}
