package com.github.jactor.rises.persistence.repository;

import com.github.jactor.rises.persistence.entity.person.PersonEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PersonRepository extends CrudRepository<PersonEntity, Long> {
    List<PersonEntity> findBySurname(String surname);
}
