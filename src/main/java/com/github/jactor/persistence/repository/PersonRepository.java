package com.github.jactor.persistence.repository;

import com.github.jactor.persistence.entity.PersonEntity;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<PersonEntity, Long> {

  List<PersonEntity> findBySurname(String surname);
}
