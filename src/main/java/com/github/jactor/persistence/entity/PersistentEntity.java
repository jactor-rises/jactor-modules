package com.github.jactor.persistence.entity;

import com.github.jactor.persistence.dto.PersistentDto;
import java.util.stream.Stream;

public interface PersistentEntity<T> extends PersistentData{

  T copy();

  PersistentDto initPersistentDto();

  Stream<PersistentData> streamSequencedDependencies();

  Long getId();

  void setId(Long id);
}
