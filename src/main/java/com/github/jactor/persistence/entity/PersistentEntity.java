package com.github.jactor.persistence.entity;

import com.github.jactor.persistence.dto.PersistentDto;

public interface PersistentEntity<T> extends PersistentData {

  T copyWithoutId();

  PersistentDto initPersistentDto();

  void modify();

  Long getId();

  void setId(Long id);
}
