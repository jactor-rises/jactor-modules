package com.github.jactorrises.model.service;

import com.github.jactorrises.client.persistence.dto.GuestBookDto;
import com.github.jactorrises.client.persistence.dto.GuestBookEntryDto;
import com.github.jactorrises.model.domain.guestbook.GuestBookDomain;
import com.github.jactorrises.model.domain.guestbook.GuestBookEntryDomain;
import com.github.jactorrises.persistence.service.GuestBookDaoService;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class GuestBookDomainService {
    private final GuestBookDaoService guestBookDaoService;

    public GuestBookDomainService(GuestBookDaoService guestBookDaoService) {
        this.guestBookDaoService = guestBookDaoService;
    }

    GuestBookDomain saveOrUpdateGuestBook(GuestBookDomain guestBookDomain) {
        return new GuestBookDomain(guestBookDaoService.saveOrUpdate(guestBookDomain.getDto()));
    }

    GuestBookEntryDomain saveOrUpdateGuestBookEntry(GuestBookEntryDomain guestBookEntryDomain) {
        return new GuestBookEntryDomain(guestBookDaoService.saveOrUpdate(guestBookEntryDomain.getDto()));
    }

    GuestBookDomain fetchGuestBook(Serializable id) {
        GuestBookDto guestBookDto = guestBookDaoService.fetch(GuestBookDto.class, id);

        return new GuestBookDomain(guestBookDto);
    }

    GuestBookEntryDomain fetchGuestBookEntry(Serializable id) {
        GuestBookEntryDto guestBookEntryDto = guestBookDaoService.fetch(GuestBookEntryDto.class, id);

        return new GuestBookEntryDomain(guestBookEntryDto);
    }
}

