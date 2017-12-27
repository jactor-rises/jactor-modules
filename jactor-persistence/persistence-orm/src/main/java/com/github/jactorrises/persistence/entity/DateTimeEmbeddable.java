package com.github.jactorrises.persistence.entity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

import static java.util.Objects.hash;

public class DateTimeEmbeddable {
    private final Date timestamp;

    @SuppressWarnings("unused") // used by hibernate
    DateTimeEmbeddable() {
        timestamp = null;
    }

    public DateTimeEmbeddable(Date timestamp) {
        this.timestamp = timestamp;
    }

    public DateTimeEmbeddable(LocalDateTime timestamp) {
        this.timestamp = Date.from(timestamp.atZone(ZoneId.systemDefault()).toInstant());
    }

    public LocalDateTime fetchLocalDateTime() {
        return timestamp != null ? convert(timestamp.toInstant()) : null;
    }

    private LocalDateTime convert(Instant instant) {
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    @Override public int hashCode() {
        return hash(timestamp);
    }

    @Override public boolean equals(Object o) {
        return o == this || o != null && o.getClass().equals(getClass()) && Objects.equals(timestamp, ((DateTimeEmbeddable) o).timestamp);
    }

    @Override public String toString() {
        return timestamp != null ? LocalDateTime.ofInstant(timestamp.toInstant(), ZoneId.systemDefault()).toString() : "null";
    }
}
