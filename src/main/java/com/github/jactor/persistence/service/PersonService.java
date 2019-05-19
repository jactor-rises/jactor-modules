package com.github.jactor.persistence.service;

import com.github.jactor.persistence.dto.PersonDto;
import com.github.jactor.persistence.entity.PersonEntity;
import com.github.jactor.persistence.repository.PersonRepository;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

  private final PersonRepository personRepository;

  public PersonService(PersonRepository personRepository) {
    this.personRepository = personRepository;
  }

  public PersonEntity create(PersonDto person) {
    PersonEntity personEntity = new PersonEntity(person);
    personRepository.save(personEntity);
    return personEntity;
  }
}
