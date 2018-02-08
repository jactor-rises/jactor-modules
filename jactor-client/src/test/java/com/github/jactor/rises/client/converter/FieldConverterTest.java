package com.github.jactor.rises.client.converter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("A FieldConverter")
class FieldConverterTest {

    @DisplayName("should convert a string to a date")
    @Test void shouldConvertFromStringToLocalDate() {
        LocalDate localDate = FieldConverter.convertDate("06.12.1974");

        assertThat(localDate).as("date").isEqualTo(LocalDate.of(1974, 12, 6));
    }

    @DisplayName("should convert a date to a string")
    @Test void shouldConvertFromLocalDateToString() {
        String date = FieldConverter.convert(LocalDate.of(1974, 12, 6));

        assertThat(date).as("date as string").isEqualTo("06.12.1974");
    }

    @DisplayName("should convert a string to a date time")
    @Test void shouldConvertFromStringToLocalDateTime() {
        LocalDateTime localDateTime = FieldConverter.convertDateTime("06.12.1974 15:20:00.000000100");

        assertThat(localDateTime).as("date time").isEqualTo(LocalDateTime
                .of(1974, 12, 6, 15, 20, 0, 100)
        );
    }

    @DisplayName("should convert a date time to a string")
    @Test void shouldConvertFromLocalDateTimeToString() {
        String date = FieldConverter.convert(LocalDateTime.of(1974, 12, 6, 15, 20, 0, 10000));

        assertThat(date).as("date time as string").isEqualTo("06.12.1974 15:20:00.000010000");
    }
}
