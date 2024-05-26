package com.github.jactor.persistence.guestbook

import java.util.UUID
import org.springframework.stereotype.Service

interface GuestBookService {
    fun find(id: UUID): GuestBookModel?
    fun findEntry(id: UUID): GuestBookEntryModel?
    fun saveOrUpdate(guestBookModel: GuestBookModel): GuestBookModel
    fun saveOrUpdate(guestBookEntryModel: GuestBookEntryModel): GuestBookEntryModel
}

@Service
class DefaultGuestBookService(
    private val guestBookRepository: GuestBookRepository,
    private val guestBookEntryRepository: GuestBookEntryRepository
): GuestBookService {
    override fun find(id: UUID): GuestBookModel? {
        return guestBookRepository.findById(id)
            .map { it.toModel() }
            .orElse(null)
    }

    override fun findEntry(id: UUID): GuestBookEntryModel? {
        return guestBookEntryRepository.findById(id)
            .map { it.toModel() }
            .orElse(null)
    }

    override fun saveOrUpdate(guestBookModel: GuestBookModel): GuestBookModel {
        return guestBookRepository.save(GuestBookEntity(guestBookModel)).toModel()
    }

    override fun saveOrUpdate(guestBookEntryModel: GuestBookEntryModel): GuestBookEntryModel {
        return guestBookEntryRepository.save(GuestBookEntryEntity(guestBookEntryModel)).toModel()
    }
}
