package com.github.jactorrises.persistence.client.dao;

import com.github.jactorrises.client.datatype.UserName;

import java.util.Optional;

public interface UserDao {

    Optional<UserEntity> findUsing(UserName userName);

    void save(UserEntity userEntity);
}
