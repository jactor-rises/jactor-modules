package com.github.jactorrises.persistence.entity.person;

import java.util.Locale;
import java.util.Objects;

import static java.util.Objects.hash;

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
        return locale != null ? new Locale(locale) : null;
    }

    @Override public boolean equals(Object o) {
        return o == this || o != null && o.getClass().equals(getClass()) && Objects.equals(locale, ((LocaleEmbeddable) o).locale);
    }

    @Override public int hashCode() {
        return hash(locale);
    }

    @Override public String toString() {
        return locale != null ? new Locale(locale).toString() : "null";
    }
}
