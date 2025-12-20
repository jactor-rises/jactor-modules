package com.github.jactor.rises.persistence.guestbook

import com.github.jactor.rises.persistence.PersistenceHandler
import org.springframework.stereotype.Service
import java.util.UUID

interface GuestBookService {
    suspend fun create(createGuestBook: CreateGuestBook): GuestBook
    suspend fun create(createGuestBookEntry: CreateGuestBookEntry): GuestBookEntry
    suspend fun findGuestBook(id: UUID): GuestBook?
    suspend fun findEntry(id: UUID): GuestBookEntry?
    suspend fun saveOrUpdate(guestBook: GuestBook): GuestBook
    suspend fun saveOrUpdate(guestBookEntry: GuestBookEntry): GuestBookEntry
}

@Service
class GuestBookServiceBean(
    private val guestBookRepository: GuestBookRepository,
    private val persistenceHandler: PersistenceHandler,
) : GuestBookService {
    override suspend fun create(createGuestBook: CreateGuestBook): GuestBook = persistenceHandler.modifyAndSave(
        dao = GuestBookDao(title = createGuestBook.title, userId = createGuestBook.userId),
    ) { guestBookRepository.save(guestBookDao = it) }.toGuestBook()

    override suspend fun create(createGuestBookEntry: CreateGuestBookEntry): GuestBookEntry {
        return persistenceHandler.modifyAndSave(
            dao = GuestBookEntryDao(
                guestName = createGuestBookEntry.creatorName,
                entry = createGuestBookEntry.entry,
                guestBookId = createGuestBookEntry.guestBookId,
            ),
        ) { guestBookRepository.save(guestBookEntryDao = it) }.toGuestBookEntry()
    }

    override suspend fun findGuestBook(id: UUID): GuestBook? {
        return guestBookRepository.findGuestBookById(id)?.toGuestBook()
    }

    override suspend fun findEntry(id: UUID): GuestBookEntry? {
        return guestBookRepository.findGuestBookEntryById(id)?.toGuestBookEntry()
    }

    override suspend fun saveOrUpdate(guestBook: GuestBook): GuestBook = persistenceHandler.modifyAndSave(
        dao = guestBook.toGuestBookDao(),
    ) { guestBookRepository.save(guestBookDao = it) }.toGuestBook()

    override suspend fun saveOrUpdate(guestBookEntry: GuestBookEntry): GuestBookEntry {
        return persistenceHandler.modifyAndSave(dao = guestBookEntry.toGuestBookEntryDao()) {
            guestBookRepository.save(guestBookEntryDao = it)
        }.toGuestBookEntry()
    }
}
