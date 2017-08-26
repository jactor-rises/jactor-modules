package nu.hjemme.persistence.orm.domain;

import nu.hjemme.persistence.orm.time.NowAsPureDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.fail;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("A DefaultPersistentEntry")
class DefaultPersistentEntryTest {

    private DefaultPersistentEntry defaultPersistentEntryToTest;

    @BeforeEach
    void initClassToTest() {
        NowAsPureDate.set();
        defaultPersistentEntryToTest = new DefaultPersistentEntry();
    }

    @DisplayName("should have an implementation of the hash code method")
    @Test
    void willHaveCorrectlyImplementedHashCode() {
        DefaultPersistentEntry base = defaultPersistentEntryToTest;
        base.setCreatorName("a creator");
        base.setEntry("some entry");

        DefaultPersistentEntry equal = new DefaultPersistentEntry(base);

        DefaultPersistentEntry notEqual = new DefaultPersistentEntry();
        notEqual.setCreatorName("another creator");
        notEqual.setEntry("another entry");

        assertAll(
                () -> assertThat(base.hashCode()).as("(%s).hashCode() is equal to (%s).hashCode()", base, equal).isEqualTo(equal.hashCode()),
                () -> assertThat(base.hashCode()).as("(%s).hashCode() is not equal to (%s).hashCode()", base, notEqual).isNotEqualTo(notEqual.hashCode()),
                () -> assertThat(base.hashCode()).as("(%s).hashCode() is a number with different value", base).isNotEqualTo(0)
        );
    }

    @DisplayName("should have an implementation of the equals method")
    @Test
    void willHaveCorrectlyImplementedEquals() {
        DefaultPersistentEntry base = defaultPersistentEntryToTest;
        base.setCreatorName("a creator");
        base.setEntry("some entry");

        DefaultPersistentEntry equal = new DefaultPersistentEntry(base);

        DefaultPersistentEntry notEqual = new DefaultPersistentEntry();
        notEqual.setCreatorName("another creator");
        notEqual.setEntry("another entry");

        assertAll(
                () -> assertThat(base).as("%s is equal to %s", base, equal).isEqualTo(equal),
                () -> assertThat(base).as("%s is not equal to %s", base, notEqual).isNotEqualTo(notEqual),
                () -> assertThat(base).as("%s is not equal to %s").isNotEqualTo(null),
                () -> assertThat(base).as("%s is equal to %s").isEqualTo(base),
                () -> assertThat(base).as("base is not same instance as equal").isNotSameAs(equal)
        );
    }

    @Test
    void shouldDisplayEntryInToString() {
        defaultPersistentEntryToTest.setEntry("hello you");
        assertThat(defaultPersistentEntryToTest.toString()).contains(",hello you");
    }

    @Test
    void shouldNotDisplayEntryInToStringWithMoreCharachtersThan50() {
        defaultPersistentEntryToTest.setEntry("123456789.123456789.123456789.123456789.ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        assertThat(defaultPersistentEntryToTest.toString()).contains(",123456789.123456789.123456789.123456789.ABCDEFG...");
    }

    @AfterEach
    void removeNowAsPureDate() {
        NowAsPureDate.remove();
    }
}
