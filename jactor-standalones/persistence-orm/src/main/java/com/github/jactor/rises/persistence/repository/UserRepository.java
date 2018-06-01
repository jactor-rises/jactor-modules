package com.github.jactor.rises.persistence.repository;

import com.github.jactor.rises.persistence.entity.user.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    Optional<UserEntity> findByUserName(String userName);
}
