package com.github.jactorrises.client.domain;

import com.github.jactorrises.client.datatype.Name;

import java.util.Locale;

public interface Person extends Persistent {

    String getDescription();

    User getUser();

    Address getAddress();

    Name getFirstName();

    Name getSurname();

    Locale getLocale();

}
