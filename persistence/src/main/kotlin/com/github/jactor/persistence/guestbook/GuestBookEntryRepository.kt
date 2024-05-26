package com.github.jactor.persistence.guestbook

import java.util.UUID
import org.springframework.data.repository.CrudRepository

interface GuestBookEntryRepository : CrudRepository<GuestBookEntryEntity, UUID> {
    fun findByGuestBook(guestBookEntity: GuestBookEntity): List<GuestBookEntryEntity>
}
