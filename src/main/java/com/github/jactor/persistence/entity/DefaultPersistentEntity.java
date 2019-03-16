package com.github.jactor.persistence.entity;

import com.github.jactor.persistence.dto.PersistentDto;
import com.github.jactor.persistence.time.Now;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class DefaultPersistentEntity implements PersistentEntity {

  @Column(name = "CREATION_TIME")
  private LocalDateTime creationTime;
  @Column(name = "CREATED_BY")
  private String createdBy;
  @Column(name = "UPDATED_TIME")
  private LocalDateTime updatedTime;
  @Column(name = "UPDATED_BY")
  private String updatedBy;

  protected DefaultPersistentEntity() {
    createdBy = "todo #3";
    creationTime = Now.asDateTime();
    updatedBy = "todo #3";
    updatedTime = Now.asDateTime();
  }

  protected DefaultPersistentEntity(DefaultPersistentEntity persistentEntity) {
    createdBy = persistentEntity.createdBy;
    creationTime = persistentEntity.creationTime;
    updatedBy = persistentEntity.updatedBy;
    updatedTime = persistentEntity.updatedTime;
  }

  protected DefaultPersistentEntity(PersistentDto persistentDto) {
    setId(persistentDto.getId());
    createdBy = persistentDto.getCreatedBy();
    creationTime = persistentDto.getCreationTime();
    updatedBy = persistentDto.getUpdatedBy();
    updatedTime = persistentDto.getUpdatedTime();
  }

  public DefaultPersistentEntity addSequencedId(Sequencer sequencer) {
    if (getId() == null) {
      addSequencedId(this, sequencer);
    }

    fetchAllPersistentEntities().stream()
        .filter(dependency -> dependency.getId() == null)
        .forEach(depencency -> addSequencedId(depencency, sequencer));

    return this;
  }

  private void addSequencedId(PersistentEntity<?> persistentEntity, Sequencer sequencer) {
    Long id = sequencer.nextVal(persistentEntity.getClass());
    persistentEntity.setId(id);
  }

  public Stream<PersistentEntity> streamSequencedDependencies(PersistentEntity... persistentEntities) {
    if (persistentEntities == null) {
      return Stream.empty();
    }

    return Arrays.stream(persistentEntities)
        .filter(Objects::nonNull);
  }

  List<PersistentEntity> fetchAllPersistentEntities() {
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

  @Override
  public String getCreatedBy() {
    return createdBy;
  }

  public LocalDateTime getCreationTime() {
    return creationTime;
  }

  @Override
  public String getUpdatedBy() {
    return updatedBy;
  }

  public LocalDateTime getUpdatedTime() {
    return updatedTime;
  }

  protected void setCreationTime(LocalDateTime creationTime) {
    this.creationTime = creationTime;
  }

  protected void setUpdatedTime(LocalDateTime updatedTime) {
    this.updatedTime = updatedTime;
  }

  public interface Sequencer {

    Long nextVal(Class<?> entityClass);
  }
}
