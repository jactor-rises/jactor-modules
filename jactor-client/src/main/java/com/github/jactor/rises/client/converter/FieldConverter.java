package com.github.jactor.rises.client.converter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class FieldConverter {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy kk:mm:ss.nnnnnnnnn");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private FieldConverter() {
        // utility class
    }

    public static String convert(LocalDateTime localDateTime) {
        return localDateTime.format(DATE_TIME_FORMATTER);
    }

    public static String convert(LocalDate localDate) {
        return localDate.format(DATE_FORMATTER);
    }

    public static LocalDateTime convertDateTime(String dateTime) {
        return LocalDateTime.parse(dateTime, DATE_TIME_FORMATTER);
    }

    public static LocalDate convertDate(String date) {
        return LocalDate.parse(date, DATE_FORMATTER);
    }
}
