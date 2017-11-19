package com.github.jactorrises.persistence.entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("A PersistentEntry")
class PersistentEntryTest {

    private PersistentEntry persistentEntryToTest;

    @BeforeEach
    void initClassToTest() {
        NowAsPureDate.set();
        persistentEntryToTest = new PersistentEntry();
    }

    @DisplayName("should have an implementation of the hash code method")
    @Test
    void willHaveCorrectlyImplementedHashCode() {
        PersistentEntry base = persistentEntryToTest;
        base.setCreatorName("a creator");
        base.setEntry("some entry");

        PersistentEntry equal = new PersistentEntry(base);

        PersistentEntry notEqual = new PersistentEntry();
        notEqual.setCreatorName("another creator");
        notEqual.setEntry("another entry");

        assertAll(
                () -> assertThat(base.hashCode()).as("base.hashCode() is equal to equal.hashCode()", base, equal).isEqualTo(equal.hashCode()),
                () -> assertThat(base.hashCode()).as("base.hashCode() is not equal to notEqual.hashCode()", base, notEqual).isNotEqualTo(notEqual.hashCode()),
                () -> assertThat(base.hashCode()).as("base.hashCode() is a number with different value", base).isNotEqualTo(0),
                () -> assertThat(base).as("base is not same instance as equal").isNotSameAs(equal)
        );
    }

    @DisplayName("should have an implementation of the equals method")
    @Test
    void willHaveCorrectlyImplementedEquals() {
        PersistentEntry base = persistentEntryToTest;
        base.setCreatorName("a creator");
        base.setEntry("some entry");

        PersistentEntry equal = new PersistentEntry(base);

        PersistentEntry notEqual = new PersistentEntry();
        notEqual.setCreatorName("another creator");
        notEqual.setEntry("another entry");

        assertAll(
                () -> assertThat(base).as("base is not equal to null").isNotEqualTo(null),
                () -> assertThat(base).as("base is equal to base").isEqualTo(base),
                () -> assertThat(base).as("base is equal to equal", base, equal).isEqualTo(equal),
                () -> assertThat(base).as("base is not equal to notEqual", base, notEqual).isNotEqualTo(notEqual),
                () -> assertThat(base).as("base is not same instance as equal").isNotSameAs(equal)
        );
    }

    @Test
    void shouldDisplayEntryInToString() {
        persistentEntryToTest.setEntry("hello you");
        assertThat(persistentEntryToTest.toString()).contains(",hello you");
    }

    @Test
    void shouldNotDisplayEntryInToStringWithMoreCharachtersThan50() {
        persistentEntryToTest.setEntry("123456789.123456789.123456789.123456789.ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        assertThat(persistentEntryToTest.toString()).contains(",123456789.123456789.123456789.123456789.ABCDEFG...");
    }

    @AfterEach
    void removeNowAsPureDate() {
        NowAsPureDate.remove();
    }
}
