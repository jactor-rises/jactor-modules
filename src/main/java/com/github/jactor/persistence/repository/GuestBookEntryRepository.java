package com.github.jactor.persistence.repository;

import com.github.jactor.persistence.entity.GuestBookEntity;
import com.github.jactor.persistence.entity.GuestBookEntryEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GuestBookEntryRepository extends CrudRepository<GuestBookEntryEntity, Long> {
    List<GuestBookEntryEntity> findByGuestBook(GuestBookEntity guestBookEntity);
}
