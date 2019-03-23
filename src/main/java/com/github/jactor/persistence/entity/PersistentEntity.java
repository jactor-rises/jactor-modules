package com.github.jactor.persistence.entity;

import com.github.jactor.persistence.dto.PersistentDto;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    fetchAllPersistentEntities().stream()
        .filter(dependency -> dependency.getId() == null)
        .filter(dependency -> !(dependency instanceof AddressEntity))
        .filter(dependency -> !(dependency instanceof GuestBookEntryEntity))
        .filter(dependency -> !(dependency instanceof PersonEntity))
        .filter(dependency -> !(dependency instanceof BlogEntryEntity))
        .filter(dependency -> !(dependency instanceof UserEntity))
        .forEach(depencency -> addSequencedId(depencency, sequencer));

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

  default List<PersistentEntity> fetchAllPersistentEntities() {
    List<PersistentEntity> allSequencedDependencies = new ArrayList<>();
    streamSequencedDependencies(this)
        .forEach(persistentData -> addAllSequencedDependencis(persistentData, allSequencedDependencies));

    return allSequencedDependencies;
  }

  private void addAllSequencedDependencis(PersistentEntity persistentEntity, List<PersistentEntity> allSequencedEntities) {
    allSequencedEntities.add(persistentEntity);
    fetchSequencedDependencies(persistentEntity)
        .filter(entityToSequence -> !allSequencedEntities.contains(entityToSequence)) // if not added by other dependency
        .forEach(entityToSequence -> addAllSequencedDependencis(entityToSequence, allSequencedEntities));
  }

  @SuppressWarnings("unchecked")
  private Stream<PersistentEntity> fetchSequencedDependencies(PersistentEntity persistentEntity) {
    return persistentEntity.streamSequencedDependencies()
        .filter(Objects::nonNull);
  }

  @FunctionalInterface
  interface Sequencer {

    Long nextVal(Class<?> entityClass);
  }
}
