package com.gitlab.jactor.rises.model.service;

import com.gitlab.jactor.rises.commons.dto.GuestBookDto;
import com.gitlab.jactor.rises.commons.dto.GuestBookEntryDto;

import java.io.Serializable;

public interface GuestBookConsumerService {
    GuestBookDto saveOrUpdate(GuestBookDto guestBookDto);

    GuestBookEntryDto saveOrUpdate(GuestBookEntryDto guestBookEntryDto);

    GuestBookDto fetch(Serializable id);

    GuestBookEntryDto fetchEntry(Serializable entryId);
}
