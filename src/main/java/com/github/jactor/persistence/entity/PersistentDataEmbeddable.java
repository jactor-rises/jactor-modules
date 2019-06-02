package com.github.jactor.persistence.entity;

import com.github.jactor.persistence.dto.PersistentDto;
import com.github.jactor.persistence.time.Now;
import java.time.LocalDateTime;
import javax.persistence.Embeddable;

@Embeddable
public class PersistentDataEmbeddable implements PersistentData {

  private String createdBy;
  private LocalDateTime timeOfCreation;
  private String modifiedBy;
  private LocalDateTime timeOfModification;

  PersistentDataEmbeddable() {
    createdBy = "todo";
    timeOfCreation = Now.asDateTime();
    modifiedBy = "todo";
    timeOfModification = Now.asDateTime();
  }

  PersistentDataEmbeddable(PersistentDto persistentDto) {
    createdBy = persistentDto.getCreatedBy();
    timeOfCreation = persistentDto.getTimeOfCreation();
    modifiedBy = persistentDto.getModifiedBy();
    timeOfModification = persistentDto.getTimeOfModification();
  }

  void modify() {
    timeOfModification = Now.asDateTime();
  }

  PersistentDto asPersistentDto(Long id) {
    return new PersistentDto(id, createdBy, timeOfCreation, modifiedBy, timeOfModification);
  }

  @Override
  public String getCreatedBy() {
    return createdBy;
  }

  @Override
  public LocalDateTime getTimeOfCreation() {
    return timeOfCreation;
  }

  @Override
  public String getModifiedBy() {
    return modifiedBy;
  }

  @Override
  public LocalDateTime getTimeOfModification() {
    return timeOfModification;
  }
}
