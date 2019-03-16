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
public abstract class PersistentEntity implements PersistentData {

  @Column(name = "CREATION_TIME")
  private LocalDateTime creationTime;
  @Column(name = "CREATED_BY")
  private String createdBy;
  @Column(name = "UPDATED_TIME")
  private LocalDateTime updatedTime;
  @Column(name = "UPDATED_BY")
  private String updatedBy;

  protected PersistentEntity() {
    createdBy = "todo #3";
    creationTime = Now.asDateTime();
    updatedBy = "todo #3";
    updatedTime = Now.asDateTime();
  }

  protected PersistentEntity(PersistentEntity persistentEntity) {
    createdBy = persistentEntity.createdBy;
    creationTime = persistentEntity.creationTime;
    updatedBy = persistentEntity.updatedBy;
    updatedTime = persistentEntity.updatedTime;
  }

  protected PersistentEntity(PersistentDto persistentDto) {
    setId(persistentDto.getId());
    createdBy = persistentDto.getCreatedBy();
    creationTime = persistentDto.getCreationTime();
    updatedBy = persistentDto.getUpdatedBy();
    updatedTime = persistentDto.getUpdatedTime();
  }

  public PersistentData addSequencedId(Sequencer sequencer) {
    if (getId() == null) {
      addSequencedId(this, sequencer);
    }

    fetchAllSequencedDependencies().stream()
        .filter(dependency -> dependency.getId() == null)
        .forEach(depencency -> addSequencedId(depencency, sequencer));

    return this;
  }

  private void addSequencedId(PersistentData<?> persistentData, Sequencer sequencer) {
    Long id = sequencer.nextVal(persistentData.getClass());
    persistentData.setId(id);
  }

  public Stream<PersistentData> streamSequencedDependencies(PersistentData... persistentData) {
    if (persistentData == null) {
      return Stream.empty();
    }

    return Arrays.stream(persistentData)
        .filter(Objects::nonNull);
  }

  List<PersistentData> fetchAllSequencedDependencies() {
    List<PersistentData> allSequencedDependencies = new ArrayList<>();
    streamSequencedDependencies(this)
        .forEach(persistentData -> addAllSequencedDependencis(persistentData, allSequencedDependencies));

    return allSequencedDependencies;
  }

  private void addAllSequencedDependencis(PersistentData dependency, List<PersistentData> allSequencedDependencies) {
    allSequencedDependencies.add(dependency);
    fetchSequencedDependencies(dependency)
        .filter(persistentData -> !allSequencedDependencies.contains(persistentData)) // if not added by other dependency
        .forEach(persistentData -> addAllSequencedDependencis(persistentData, allSequencedDependencies));
  }

  @SuppressWarnings("unchecked")
  private Stream<PersistentData> fetchSequencedDependencies(PersistentData persistentData) {
    return persistentData.streamSequencedDependencies()
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
