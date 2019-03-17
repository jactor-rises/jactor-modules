package com.github.jactor.persistence.entity;

import java.time.LocalDateTime;

public interface PersistentData {

  String getCreatedBy();

  LocalDateTime getTimeOfCreation();

  String getModifiedBy();

  LocalDateTime getTimeOfModification();
}
