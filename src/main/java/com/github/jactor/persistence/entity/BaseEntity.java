package com.github.jactor.persistence.entity;

import com.github.jactor.persistence.dto.PersistentDto;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;

public interface BaseEntity {

  BaseEntity copy();

  PersistentDto initPersistentDto();

  Stream<Optional<PersistentEntity>> streamSequencedDependencies();

  String getCreatedBy();

  LocalDateTime getCreationTime();

  Long getId();

  String getUpdatedBy();

  LocalDateTime getUpdatedTime();

  void setId(Long id);
}
