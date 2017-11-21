package com.github.jactorrises.persistence.entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static java.util.Objects.hash;

public class DateTextEmbeddable {
    private static final DateTimeFormatter DD_MM_YYYY = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final String dateAsText;

    @SuppressWarnings("unused") // used by hibernate
    DateTextEmbeddable() {
        dateAsText = null;
    }

    public DateTextEmbeddable(LocalDate date) {
        dateAsText = date.format(DD_MM_YYYY);
    }

    public LocalDate fetchLocalDate() {
        return LocalDate.parse(dateAsText, DD_MM_YYYY);
    }

    @Override public int hashCode() {
        return hash(dateAsText);
    }

    @Override public boolean equals(Object o) {
        return o == this || o != null && o.getClass().equals(getClass()) && Objects.equals(dateAsText, ((DateTextEmbeddable) o).dateAsText);
    }

    @Override public String toString() {
        return dateAsText;
    }
}
