package com.gitlab.jactor.persistence.repository;

import com.gitlab.jactor.persistence.entity.guestbook.GuestBookEntity;
import com.gitlab.jactor.persistence.entity.guestbook.GuestBookEntryEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GuestBookEntryRepository extends CrudRepository<GuestBookEntryEntity, Long> {
    List<GuestBookEntryEntity> findByGuestBook(GuestBookEntity guestBookEntity);
}
