package com.github.jactor.persistence.guestbook

import java.util.UUID
import org.springframework.stereotype.Repository

interface GuestBookRepository {
    fun findAllGuestBooks(): List<GuestBookDao>
    fun findByUserId(userId: UUID): GuestBookDao?
    fun findGuestBookById(id: UUID): GuestBookDao?
    fun findGuestBookEntryById(id: UUID): GuestBookEntryDao?
    fun findGuestBookByUserId(id: UUID): GuestBookDao?
    fun findGuestBookEtriesByGuestBookId(id: UUID): List<GuestBookEntryDao>
    fun save(guestBookDao: GuestBookDao): GuestBookDao
    fun save(guestBookEntryDao: GuestBookEntryDao): GuestBookEntryDao
}

@Repository
class GuestBookRepositoryImpl : GuestBookRepository by GuestBookRepositoryObject
