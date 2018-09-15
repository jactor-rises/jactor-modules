package com.gitlab.jactor.rises.model.domain;

import java.util.Set;

public interface GuestBook extends Persistent {

    String getTitle();

    User getUser();

    Set<GuestBookEntry> getEntries();
}
