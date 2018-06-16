package com.github.jactor.rises.model.service;

import com.github.jactor.rises.client.dto.GuestBookDto;
import com.github.jactor.rises.client.dto.GuestBookEntryDto;
import com.github.jactor.rises.io.rest.GuestBookRestService;
import com.github.jactor.rises.model.domain.guestbook.GuestBookDomain;
import com.github.jactor.rises.model.domain.guestbook.GuestBookEntryDomain;

import java.io.Serializable;
import java.util.Optional;

public class GuestBookDomainService {
    private final GuestBookRestService guestBookRestService;

    public GuestBookDomainService(GuestBookRestService guestBookRestService) {
        this.guestBookRestService = guestBookRestService;
    }

    public GuestBookDomain saveOrUpdate(GuestBookDomain guestBookDomain) {
        return new GuestBookDomain(guestBookRestService.saveOrUpdate(guestBookDomain.getDto()));
    }

    public GuestBookEntryDomain saveOrUpdateEntry(GuestBookEntryDomain guestBookEntryDomain) {
        return new GuestBookEntryDomain(guestBookRestService.saveOrUpdate(guestBookEntryDomain.getDto()));
    }

    public Optional<GuestBookDomain> find(Serializable id) {
        GuestBookDto guestBookDto = guestBookRestService.fetch(id);

        return Optional.ofNullable(guestBookDto).map(GuestBookDomain::new);
    }

    public Optional<GuestBookEntryDomain> findEntry(Serializable id) {
        GuestBookEntryDto guestBookEntryDto = guestBookRestService.fetchEntry(id);

        return Optional.ofNullable(guestBookEntryDto).map(GuestBookEntryDomain::new);
    }
}

