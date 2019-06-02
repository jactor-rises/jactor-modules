package com.github.jactor.persistence.entity;

public interface PersistentEntity<T> extends PersistentData {

  T copyWithoutId();

  void modify();

  Long getId();

  void setId(Long id);
}
