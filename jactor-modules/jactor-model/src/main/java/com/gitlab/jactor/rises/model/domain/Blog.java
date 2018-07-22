package com.gitlab.jactor.rises.model.domain;

import java.time.LocalDate;

public interface Blog extends Persistent {
    String getTitle();

    User getUser();

    LocalDate getCreated();
}
