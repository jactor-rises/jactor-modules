package com.github.jactorrises.client.domain;

import com.github.jactorrises.client.datatype.Name;

import java.time.LocalDateTime;

public interface GuestBookEntry extends Persistent {

    GuestBook getGuestBook();

    LocalDateTime getCreatedTime();

    String getEntry();

    Name getCreatorName();
}

