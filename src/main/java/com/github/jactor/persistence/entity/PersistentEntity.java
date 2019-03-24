package com.github.jactor.persistence.entity;

import com.github.jactor.persistence.dto.PersistentDto;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

public interface PersistentEntity<T> extends PersistentData {

  T copyWithoutId();

  PersistentDto initPersistentDto();

  void modify();

  Stream<PersistentEntity> streamSequencedDependencies();

  Long getId();

  void setId(Long id);

  default PersistentEntity addSequencedId(Sequencer sequencer) {
    if (getId() == null) {
      addSequencedId(this, sequencer);
    }

    return this;
  }

  private void addSequencedId(PersistentEntity persistentEntity, Sequencer sequencer) {
    Long id = sequencer.nextVal(persistentEntity.getClass());
    persistentEntity.setId(id);
  }

  default Stream<PersistentEntity> streamSequencedDependencies(PersistentEntity... persistentEntities) {
    if (persistentEntities == null) {
      return Stream.empty();
    }

    return Arrays.stream(persistentEntities)
        .filter(Objects::nonNull);
  }

  @FunctionalInterface
  interface Sequencer {

    Long nextVal(Class<?> entityClass);
  }
}
