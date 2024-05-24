package com.github.jactor.persistence.service

import java.util.UUID
import org.springframework.stereotype.Service
import com.github.jactor.persistence.dto.GuestBookModel
import com.github.jactor.persistence.dto.GuestBookEntryModel
import com.github.jactor.persistence.entity.GuestBookEntity
import com.github.jactor.persistence.entity.GuestBookEntryEntity
import com.github.jactor.persistence.repository.GuestBookEntryRepository
import com.github.jactor.persistence.repository.GuestBookRepository

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
            .map { it.asDto() }
            .orElse(null)
    }

    override fun findEntry(id: UUID):GuestBookEntryModel? {
        return guestBookEntryRepository.findById(id)
            .map { it.asDto() }
            .orElse(null)
    }

    override fun saveOrUpdate(guestBookModel: GuestBookModel): GuestBookModel {
        return guestBookRepository.save(GuestBookEntity(guestBookModel)).asDto()
    }

    override fun saveOrUpdate(guestBookEntryModel: GuestBookEntryModel): GuestBookEntryModel {
        return guestBookEntryRepository.save(GuestBookEntryEntity(guestBookEntryModel)).asDto()
    }
}
