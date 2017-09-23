package com.github.jactorrises.model.persistence.entity.person;

import java.util.Locale;

class LocaleEmbeddable {
    private final String locale;

    @SuppressWarnings("unused") // used by bibernate
    public LocaleEmbeddable() {
        locale = null;
    }

    LocaleEmbeddable(Locale locale) {
        this.locale = locale != null ? locale.toString() : null;
    }

    Locale fetchLocale() {
        return new Locale(locale);
    }
}
