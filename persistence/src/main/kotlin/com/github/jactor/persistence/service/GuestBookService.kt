package com.github.jactor.persistence.service

import java.util.UUID
import org.springframework.stereotype.Service
import com.github.jactor.persistence.dto.GuestBookDto
import com.github.jactor.persistence.dto.GuestBookEntryDto
import com.github.jactor.persistence.entity.GuestBookEntity
import com.github.jactor.persistence.entity.GuestBookEntryEntity
import com.github.jactor.persistence.repository.GuestBookEntryRepository
import com.github.jactor.persistence.repository.GuestBookRepository

interface GuestBookService {
    fun find(id: UUID): GuestBookDto?
    fun findEntry(id: UUID): GuestBookEntryDto?
    fun saveOrUpdate(guestBookDto: GuestBookDto): GuestBookDto
    fun saveOrUpdate(guestBookEntryDto: GuestBookEntryDto): GuestBookEntryDto
}

@Service
class DefaultGuestBookService(
    private val guestBookRepository: GuestBookRepository,
    private val guestBookEntryRepository: GuestBookEntryRepository
): GuestBookService {
    override fun find(id: UUID): GuestBookDto? {
        return guestBookRepository.findById(id)
            .map { it.asDto() }
            .orElse(null)
    }

    override fun findEntry(id: UUID):GuestBookEntryDto? {
        return guestBookEntryRepository.findById(id)
            .map { it.asDto() }
            .orElse(null)
    }

    override fun saveOrUpdate(guestBookDto: GuestBookDto): GuestBookDto {
        return guestBookRepository.save(GuestBookEntity(guestBookDto)).asDto()
    }

    override fun saveOrUpdate(guestBookEntryDto: GuestBookEntryDto): GuestBookEntryDto {
        return guestBookEntryRepository.save(GuestBookEntryEntity(guestBookEntryDto)).asDto()
    }
}
