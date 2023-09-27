package com.github.jactor.persistence.service

import org.springframework.stereotype.Service
import com.github.jactor.persistence.dto.GuestBookDto
import com.github.jactor.persistence.dto.GuestBookEntryDto
import com.github.jactor.persistence.entity.GuestBookEntity
import com.github.jactor.persistence.entity.GuestBookEntryEntity
import com.github.jactor.persistence.repository.GuestBookEntryRepository
import com.github.jactor.persistence.repository.GuestBookRepository

@Service
class GuestBookService(
    private val guestBookRepository: GuestBookRepository,
    private val guestBookEntryRepository: GuestBookEntryRepository
) {
    fun find(id: Long): GuestBookDto? {
        return guestBookRepository.findById(id)
            .map { it.asDto() }
            .orElse(null)
    }

    fun findEntry(id: Long):GuestBookEntryDto? {
        return guestBookEntryRepository.findById(id)
            .map { it.asDto() }
            .orElse(null)
    }

    fun saveOrUpdate(guestBookDto: GuestBookDto): GuestBookDto {
        return guestBookRepository.save(GuestBookEntity(guestBookDto)).asDto()
    }

    fun saveOrUpdate(guestBookEntryDto: GuestBookEntryDto): GuestBookEntryDto {
        return guestBookEntryRepository.save(GuestBookEntryEntity(guestBookEntryDto)).asDto()
    }
}
