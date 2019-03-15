package com.github.jactor.persistence.repository;

import com.github.jactor.persistence.entity.GuestBookEntity;
import com.github.jactor.persistence.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface GuestBookRepository extends CrudRepository<GuestBookEntity, Long> {

  GuestBookEntity findByUser(UserEntity userEntity);
}
