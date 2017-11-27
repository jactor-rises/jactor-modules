package com.github.jactorrises.persistence.client.dao;

import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.client.domain.Persistent;
import com.github.jactorrises.persistence.client.entity.UserEntity;

import java.io.Serializable;
import java.util.Optional;

public interface PersistentDao {

    Optional<UserEntity> findUsing(UserName userName);

    <T extends Persistent<?>> T saveOrUpdate(T entity);

    <T extends Persistent<I>, I extends Serializable> T load(Class<T> entityClass, I id);
}
