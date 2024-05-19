package com.github.jactor.persistence.repository

import java.util.UUID
import com.github.jactor.persistence.entity.GuestBookEntity
import com.github.jactor.persistence.entity.GuestBookEntryEntity
import org.springframework.data.repository.CrudRepository

interface GuestBookEntryRepository : CrudRepository<GuestBookEntryEntity, UUID> {
    fun findByGuestBook(guestBookEntity: GuestBookEntity): List<GuestBookEntryEntity>
}
