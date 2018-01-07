package com.github.jactorrises.persistence.orm.entity;

import com.github.jactorrises.test.extension.NowAsPureDateExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("An EntryEmbeddable")
@ExtendWith(NowAsPureDateExtension.class)
class EntryEmbeddableTest {

    private EntryEmbeddable persistentEntryToTest;

    @BeforeEach
    void initClassToTest() {
        persistentEntryToTest = new EntryEmbeddable();
    }

    @DisplayName("should have an implementation of the hash code method")
    @Test
    void willHaveCorrectlyImplementedHashCode() {
        EntryEmbeddable base = persistentEntryToTest;
        base.setCreatorName("a creator");
        base.setEntry("some entry");

        EntryEmbeddable equal = new EntryEmbeddable(base);

        EntryEmbeddable notEqual = new EntryEmbeddable();
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
        EntryEmbeddable base = persistentEntryToTest;
        base.setCreatorName("a creator");
        base.setEntry("some entry");

        EntryEmbeddable equal = new EntryEmbeddable(base);

        EntryEmbeddable notEqual = new EntryEmbeddable();
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
}
