package com.gitlab.jactor.rises.persistence.repository;

import com.gitlab.jactor.rises.persistence.entity.guestbook.GuestBookEntity;
import com.gitlab.jactor.rises.persistence.entity.user.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface GuestBookRepository extends CrudRepository<GuestBookEntity, Long> {
    GuestBookEntity findByUser(UserEntity userEntity);
}
