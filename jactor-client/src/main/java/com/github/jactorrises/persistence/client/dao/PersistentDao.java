package com.github.jactorrises.persistence.client.dao;

import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.persistence.client.entity.PersistentEntity;
import com.github.jactorrises.persistence.client.entity.UserEntity;

import java.io.Serializable;
import java.util.Optional;

public interface PersistentDao {

    Optional<UserEntity> findUsing(UserName userName);

    <I, T extends PersistentEntity<I>> T saveOrUpdate(T entity);

    <T extends PersistentEntity<I>, I extends Serializable> T load(Class<T> entityClass, I id);
}
