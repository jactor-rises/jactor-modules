package com.gitlab.jactor.rises.model.service;

import com.gitlab.jactor.rises.commons.dto.GuestBookDto;
import com.gitlab.jactor.rises.commons.dto.GuestBookEntryDto;
import com.gitlab.jactor.rises.model.domain.guestbook.GuestBookDomain;
import com.gitlab.jactor.rises.model.domain.guestbook.GuestBookEntryDomain;

import java.io.Serializable;
import java.util.Optional;

public class GuestBookDomainService {
    private final GuestBookConsumerService guestBookConsumerService;

    public GuestBookDomainService(GuestBookConsumerService guestBookConsumerService) {
        this.guestBookConsumerService = guestBookConsumerService;
    }

    public GuestBookDomain saveOrUpdate(GuestBookDomain guestBookDomain) {
        return new GuestBookDomain(guestBookConsumerService.saveOrUpdate(guestBookDomain.getDto()));
    }

    public GuestBookEntryDomain saveOrUpdateEntry(GuestBookEntryDomain guestBookEntryDomain) {
        return new GuestBookEntryDomain(guestBookConsumerService.saveOrUpdate(guestBookEntryDomain.getDto()));
    }

    public Optional<GuestBookDomain> find(Serializable id) {
        GuestBookDto guestBookDto = guestBookConsumerService.fetch(id);

        return Optional.ofNullable(guestBookDto).map(GuestBookDomain::new);
    }

    public Optional<GuestBookEntryDomain> findEntry(Serializable id) {
        GuestBookEntryDto guestBookEntryDto = guestBookConsumerService.fetchEntry(id);

        return Optional.ofNullable(guestBookEntryDto).map(GuestBookEntryDomain::new);
    }
}

