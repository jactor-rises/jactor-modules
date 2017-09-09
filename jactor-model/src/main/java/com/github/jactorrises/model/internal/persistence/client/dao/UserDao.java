package com.github.jactorrises.model.internal.persistence.client.dao;

import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.model.internal.persistence.entity.user.UserEntity;

import java.util.Optional;

public interface UserDao {

    Optional<UserEntity> findUsing(UserName userName);

    void save(UserEntity userEntity);
}
