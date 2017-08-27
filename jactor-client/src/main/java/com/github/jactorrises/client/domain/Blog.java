package com.github.jactorrises.client.domain;

import java.time.LocalDate;

public interface Blog extends Persistent<Long> {
    String getTitle();

    User getUser();

    LocalDate getCreated();
}
