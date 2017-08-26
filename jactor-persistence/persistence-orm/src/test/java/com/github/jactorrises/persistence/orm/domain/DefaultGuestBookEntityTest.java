package nu.hjemme.persistence.orm.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("A DefaultGuestBookEntity")
class DefaultGuestBookEntityTest {

    private DefaultGuestBookEntity defaultGuestBookEntityToTest;

    @BeforeEach void initClassToTest() {
        defaultGuestBookEntityToTest = new DefaultGuestBookEntity();
    }

    @DisplayName("should have an implementation of the hash code method")
    @Test void willHaveCorrectImplementedHashCode() {
        DefaultGuestBookEntity base = defaultGuestBookEntityToTest;
        base.setTitle("title");
        base.setUser(new DefaultUserEntity());

        DefaultGuestBookEntity equal = new DefaultGuestBookEntity(base);

        DefaultGuestBookEntity notEqual = new DefaultGuestBookEntity();
        notEqual.setTitle("another title");
        notEqual.setUser(new DefaultUserEntity());


        assertAll(
                () -> assertThat(base.hashCode()).as("(%s).hashCode() is equal to (%s).hashCode()", base, equal).isEqualTo(equal.hashCode()),
                () -> assertThat(base.hashCode()).as("(%s).hashCode() is not equal to (%s).hashCode()", base, notEqual).isNotEqualTo(notEqual.hashCode()),
                () -> assertThat(base.hashCode()).as("(%s).hashCode() is a number with different value", base).isNotEqualTo(0)
        );
    }

    @DisplayName("should have an implementation of the equals method")
    @Test void willHaveCorrectImplementedEquals() {
        DefaultGuestBookEntity base = defaultGuestBookEntityToTest;
        base.setTitle("title");
        base.setUser(new DefaultUserEntity());

        DefaultGuestBookEntity equal = new DefaultGuestBookEntity(base);

        DefaultGuestBookEntity notEqual = new DefaultGuestBookEntity();
        notEqual.setTitle("another title");
        notEqual.setUser(new DefaultUserEntity());

        assertAll(
                () -> assertThat(base).as("%s is equal to %s", base, equal).isEqualTo(equal),
                () -> assertThat(base).as("%s is not equal to %s", base, notEqual).isNotEqualTo(notEqual),
                () -> assertThat(base).as("%s is not equal to %s").isNotEqualTo(null),
                () -> assertThat(base).as("%s is equal to %s").isEqualTo(base),
                () -> assertThat(base).as("base is not same instance as equal").isNotSameAs(equal)
        );
    }
}
