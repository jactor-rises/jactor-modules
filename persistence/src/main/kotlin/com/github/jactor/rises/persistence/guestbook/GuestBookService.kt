package com.github.jactor.rises.persistence.guestbook

import java.util.UUID
import org.springframework.stereotype.Service

interface GuestBookService {
    suspend fun create(createGuestBook: CreateGuestBook): GuestBook
    suspend fun create(createGuestBookEntry: CreateGuestBookEntry): GuestBookEntry
    suspend fun findGuestBook(id: UUID): GuestBook?
    suspend fun findEntry(id: UUID): GuestBookEntry?
    suspend fun saveOrUpdate(guestBook: GuestBook): GuestBook
    suspend fun saveOrUpdate(guestBookEntry: GuestBookEntry): GuestBookEntry
}

@Service
class GuestBookServiceBean(private val guestBookRepository: GuestBookRepository) : GuestBookService {
    override suspend fun create(createGuestBook: CreateGuestBook): GuestBook {
        return guestBookRepository.save(
            guestBookDao = GuestBookDao().apply {
                title = createGuestBook.title
                userId = createGuestBook.userId
            }
        ).toGuestBook()
    }

    override suspend fun create(createGuestBookEntry: CreateGuestBookEntry): GuestBookEntry {
        return guestBookRepository.save(
            guestBookEntryDao = GuestBookEntryDao(
                guestName = createGuestBookEntry.creatorName,
                entry = createGuestBookEntry.entry,
                guestBookId = createGuestBookEntry.guestBookId
            )
        ).toGuestBookEntry()
    }

    override suspend fun findGuestBook(id: UUID): GuestBook? {
        return guestBookRepository.findGuestBookById(id)?.toGuestBook()
    }

    override suspend fun findEntry(id: UUID): GuestBookEntry? {
        return guestBookRepository.findGuestBookEntryById(id)?.toGuestBookEntry()
    }

    override suspend fun saveOrUpdate(guestBook: GuestBook): GuestBook {
        return guestBookRepository.save(guestBookDao = guestBook.toGuestBookDao()).toGuestBook()
    }

    override suspend fun saveOrUpdate(guestBookEntry: GuestBookEntry): GuestBookEntry {
        return guestBookRepository.save(guestBookEntryDao = guestBookEntry.toGuestBookEntryDao()).toGuestBookEntry()
    }
}
