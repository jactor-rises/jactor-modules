package com.github.jactor.rises.persistence.service;

import com.github.jactor.rises.client.dto.GuestBookDto;
import com.github.jactor.rises.client.dto.GuestBookEntryDto;
import com.github.jactor.rises.persistence.entity.guestbook.GuestBookEntity;
import com.github.jactor.rises.persistence.entity.guestbook.GuestBookEntryEntity;
import com.github.jactor.rises.persistence.repository.GuestBookEntryRepository;
import com.github.jactor.rises.persistence.repository.GuestBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GuestBookService {

    private final GuestBookRepository guestBookRepository;
    private final GuestBookEntryRepository guestBookEntryRepository;

    @Autowired
    public GuestBookService(GuestBookRepository guestBookRepository, GuestBookEntryRepository guestBookEntryRepository) {
        this.guestBookRepository = guestBookRepository;
        this.guestBookEntryRepository = guestBookEntryRepository;
    }

    public Optional<GuestBookDto> find(Long id) {
        return guestBookRepository.findById(id).map(GuestBookEntity::asDto);
    }

    public Optional<GuestBookEntryDto> findEntry(Long id) {
        return guestBookEntryRepository.findById(id).map(GuestBookEntryEntity::asDto);
    }

    public GuestBookDto saveOrUpdate(GuestBookDto guestBookDto) {
        GuestBookEntity guestBookEntity = new GuestBookEntity(guestBookDto);
        guestBookRepository.save(guestBookEntity);

        return guestBookEntity.asDto();
    }

    public GuestBookEntryDto saveOrUpdate(GuestBookEntryDto guestBookEntryDto) {
        GuestBookEntryEntity guestBookEntryEntity = new GuestBookEntryEntity(guestBookEntryDto);
        guestBookEntryRepository.save(guestBookEntryEntity);

        return guestBookEntryEntity.asDto();
    }
}
