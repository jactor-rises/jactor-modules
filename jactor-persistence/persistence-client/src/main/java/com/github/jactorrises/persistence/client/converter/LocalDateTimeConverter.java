package com.github.jactorrises.persistence.client.converter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class LocalDateTimeConverter implements TypeConverter<LocalDateTime, Date> {
    @Override public LocalDateTime convertTo(Date from) {
        Date date = from;

        if (from instanceof java.sql.Date) {
            long time = from.getTime();
            date = new Date(time);
        } else if (from == null) {
            return null;
        }

        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    @Override public Date convertFrom(LocalDateTime localDateTime) {
        return localDateTime != null ? Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()) : null;
    }
}
