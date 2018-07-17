package com.gitlab.jactor.rises.persistence.repository;

import com.gitlab.jactor.rises.persistence.entity.user.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    List<UsernameProjection> findByInactiveOrderByUsername(boolean inactive);

    interface UsernameProjection {
        @Value("#{target.username}")
        String getUsername();
    }
}
