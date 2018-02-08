package com.github.jactor.rises.client.domain;

import java.util.Set;

public interface GuestBook extends Persistent {

    String getTitle();

    User getUser();

    Set<GuestBookEntry> getEntries();
}
