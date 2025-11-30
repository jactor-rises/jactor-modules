package com.github.jactor.rises.persistence.guestbook

import org.springframework.stereotype.Repository
import java.util.UUID

interface GuestBookRepository {
    fun findAllGuestBooks(): List<GuestBookDao>
    fun findByUserId(userId: UUID): GuestBookDao?
    fun findGuestBookById(id: UUID): GuestBookDao?
    fun findGuestBookEntryById(id: UUID): GuestBookEntryDao?
    fun findGuestBookEtriesByGuestBookId(id: UUID): List<GuestBookEntryDao>
    fun save(guestBookDao: GuestBookDao): GuestBookDao
    fun save(guestBookEntryDao: GuestBookEntryDao): GuestBookEntryDao
}

@Repository
class GuestBookRepositoryImpl : GuestBookRepository by GuestBookRepositoryObject
