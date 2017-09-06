package com.github.jactorrises.model.business.persistence.client.dao;

import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.model.business.persistence.entity.user.UserEntity;

import java.util.Optional;

public interface UserDao {

    Optional<UserEntity> findUsing(UserName userName);

    void save(UserEntity userEntity);
}
