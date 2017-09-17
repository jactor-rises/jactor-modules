package com.github.jactorrises.model.persistence.client.converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateConverter implements TypeConverter<LocalDate, String> {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Override public LocalDate convertTo(String date) {
        return LocalDate.parse(date, DATE_TIME_FORMATTER);
    }

    @Override public String convertFrom(LocalDate localDate) {
        return localDate != null ? localDate.format(DATE_TIME_FORMATTER) : null;
    }
}
