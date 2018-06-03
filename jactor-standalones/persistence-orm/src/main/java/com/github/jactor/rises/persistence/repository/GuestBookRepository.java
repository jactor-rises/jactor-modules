package com.github.jactor.rises.persistence.repository;

import com.github.jactor.rises.persistence.entity.guestbook.GuestBookEntity;
import com.github.jactor.rises.persistence.entity.user.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface GuestBookRepository extends CrudRepository<GuestBookEntity, Long> {
    GuestBookEntity findByUser(UserEntity userEntity);
}
