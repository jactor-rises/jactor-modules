package com.github.jactor.persistence.entity;

import java.time.LocalDateTime;

public interface PersistentData {

  String getCreatedBy();

  LocalDateTime getCreationTime();

  String getUpdatedBy();

  LocalDateTime getUpdatedTime();
}
