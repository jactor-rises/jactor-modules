package com.github.jactorrises.client.domain;

public interface GuestBook extends Persistent {

    String getTitle();

    User getUser();
}
