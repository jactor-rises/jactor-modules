package com.github.jactorrises.persistence.client.dao;

import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.client.domain.Persistent;
import com.github.jactorrises.persistence.client.dto.UserDto;

import java.io.Serializable;
import java.util.Optional;

public interface PersistentDao {

    Optional<UserDto> findUsing(UserName userName);

    <T extends Persistent<?>> T saveOrUpdate(T entity);

    <T extends Persistent<I>, I extends Serializable> T fetch(Class<T> entityClass, I id);
}
