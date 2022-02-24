package com.github.jactor.persistence.service

import com.github.jactor.persistence.dto.GuestBookDto
import com.github.jactor.persistence.dto.GuestBookEntryDto
import com.github.jactor.persistence.entity.GuestBookEntity
import com.github.jactor.persistence.entity.GuestBookEntryEntity
import com.github.jactor.persistence.repository.GuestBookEntryRepository
import com.github.jactor.persistence.repository.GuestBookRepository
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class GuestBookService(
    private val guestBookRepository: GuestBookRepository,
    private val guestBookEntryRepository: GuestBookEntryRepository
) {
    fun find(id: Long): Optional<GuestBookDto> {
        return guestBookRepository.findById(id).map { obj: GuestBookEntity? -> obj?.asDto() }
    }

    fun findEntry(id: Long): Optional<GuestBookEntryDto> {
        return guestBookEntryRepository.findById(id).map { obj: GuestBookEntryEntity? -> obj?.asDto() }
    }

    fun saveOrUpdate(guestBookDto: GuestBookDto): GuestBookDto {
        return guestBookRepository.save(GuestBookEntity(guestBookDto)).asDto()
    }

    fun saveOrUpdate(guestBookEntryDto: GuestBookEntryDto): GuestBookEntryDto {
        return guestBookEntryRepository.save(GuestBookEntryEntity(guestBookEntryDto)).asDto()
    }
}
