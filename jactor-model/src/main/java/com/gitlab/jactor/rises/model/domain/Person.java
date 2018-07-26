package com.gitlab.jactor.rises.model.domain;

import com.gitlab.jactor.rises.commons.datatype.Name;

import java.util.Locale;

public interface Person extends Persistent {

    String getDescription();

    Address getAddress();

    Name getFirstName();

    Name getSurname();

    Locale getLocale();

}
