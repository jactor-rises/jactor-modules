package com.github.jactor.persistence.repository

import com.github.jactor.persistence.entity.GuestBookEntity
import com.github.jactor.persistence.entity.GuestBookEntryEntity
import org.springframework.data.repository.CrudRepository

interface GuestBookEntryRepository : CrudRepository<GuestBookEntryEntity, Long> {
    fun findByGuestBook(guestBookEntity: GuestBookEntity): List<GuestBookEntryEntity>
}
