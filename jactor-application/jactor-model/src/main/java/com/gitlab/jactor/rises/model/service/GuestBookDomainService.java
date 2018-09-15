package com.gitlab.jactor.rises.model.service;

import com.gitlab.jactor.rises.commons.dto.GuestBookDto;
import com.gitlab.jactor.rises.commons.dto.GuestBookEntryDto;
import com.gitlab.jactor.rises.model.domain.guestbook.GuestBookDomain;
import com.gitlab.jactor.rises.model.domain.guestbook.GuestBookEntryDomain;

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

