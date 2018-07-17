package com.gitlab.jactor.rises.persistence.repository;

import com.gitlab.jactor.rises.persistence.entity.guestbook.GuestBookEntity;
import com.gitlab.jactor.rises.persistence.entity.guestbook.GuestBookEntryEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GuestBookEntryRepository extends CrudRepository<GuestBookEntryEntity, Long> {
    List<GuestBookEntryEntity> findByGuestBook(GuestBookEntity guestBookEntity);
}
