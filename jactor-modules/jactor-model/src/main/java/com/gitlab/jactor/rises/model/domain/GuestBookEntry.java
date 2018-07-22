package com.gitlab.jactor.rises.model.domain;

import com.gitlab.jactor.rises.model.datatype.Name;

import java.time.LocalDateTime;

public interface GuestBookEntry extends Persistent {

    GuestBook getGuestBook();

    LocalDateTime getCreatedTime();

    String getEntry();

    Name getCreatorName();
}

