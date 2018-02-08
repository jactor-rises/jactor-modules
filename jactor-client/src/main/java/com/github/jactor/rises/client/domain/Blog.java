package com.github.jactor.rises.client.domain;

import java.time.LocalDate;
import java.util.Set;

public interface Blog extends Persistent {
    String getTitle();

    User getUser();

    LocalDate getCreated();

    Set<BlogEntry> getEntries();
}
