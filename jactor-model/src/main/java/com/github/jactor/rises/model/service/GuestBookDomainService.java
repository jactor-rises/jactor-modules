package com.github.jactorrises.model.service;

import com.github.jactorrises.client.dto.GuestBookDto;
import com.github.jactorrises.client.dto.GuestBookEntryDto;
import com.github.jactorrises.model.domain.guestbook.GuestBookDomain;
import com.github.jactorrises.model.domain.guestbook.GuestBookEntryDomain;
import com.github.jactorrises.persistence.beans.service.GuestBookRestService;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class GuestBookDomainService {
    private final GuestBookRestService guestBookRestService;

    public GuestBookDomainService(GuestBookRestService guestBookRestService) {
        this.guestBookRestService = guestBookRestService;
    }

    GuestBookDomain saveOrUpdateGuestBook(GuestBookDomain guestBookDomain) {
        return new GuestBookDomain(guestBookRestService.saveOrUpdate(guestBookDomain.getDto()));
    }

    GuestBookEntryDomain saveOrUpdateGuestBookEntry(GuestBookEntryDomain guestBookEntryDomain) {
        return new GuestBookEntryDomain(guestBookRestService.saveOrUpdate(guestBookEntryDomain.getDto()));
    }

    GuestBookDomain fetchGuestBook(Serializable id) {
        GuestBookDto guestBookDto = guestBookRestService.fetch(id);

        return new GuestBookDomain(guestBookDto);
    }

    GuestBookEntryDomain fetchGuestBookEntry(Serializable id) {
        GuestBookEntryDto guestBookEntryDto = guestBookRestService.fetchEntry(id);

        return new GuestBookEntryDomain(guestBookEntryDto);
    }
}

