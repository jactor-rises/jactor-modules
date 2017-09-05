package com.github.jactorrises.client.domain;

public interface GuestBookEntry extends Persistent<Long>, Entry {

    GuestBook getGuestBook();
}

