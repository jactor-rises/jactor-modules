package com.github.jactorrises.client.domain;

public interface GuestBook extends Persistent<Long> {

    String getTitle();

    User getUser();
}
