package nu.hjemme.persistence.orm.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("A DefaultAddressEntity")
class DefaultAddressEntityTest {

    private DefaultAddressEntity defaultAddressEntityToTest;

    @BeforeEach void initDefaultAddressEntity() {
        defaultAddressEntityToTest = new DefaultAddressEntity();
    }

    @DisplayName("should have an implementation of the hash code method")
    @Test void willHaveCorrectImplementedHashCode() {
        DefaultAddressEntity base = defaultAddressEntityToTest;
        base.setAddressLine1("somewhere");
        base.setZipCode(1234);
        base.setCountry("NO");
        base.setCity("some city");
        base.setAddressLine2("somewhere else");
        base.setAddressLine3("way out there");

        DefaultAddressEntity equal = new DefaultAddressEntity(base);

        DefaultAddressEntity notEqual = new DefaultAddressEntity();
        notEqual.setAddressLine1("somewhere else");
        notEqual.setZipCode(5678);
        notEqual.setCountry("SE");
        notEqual.setCity("some other city");
        notEqual.setAddressLine2("some place");
        notEqual.setAddressLine3("in the distance");

        assertAll(
                () -> assertThat(base.hashCode()).as("(%s).hashCode() is equal to (%s).hashCode()", base, equal).isEqualTo(equal.hashCode()),
                () -> assertThat(base.hashCode()).as("(%s).hashCode() is not equal to (%s).hashCode()", base, notEqual).isNotEqualTo(notEqual.hashCode()),
                () -> assertThat(base.hashCode()).as("(%s).hashCode() is a number with different value", base).isNotEqualTo(0)
        );
    }

    @DisplayName("should have an implementation of the equals method")
    @Test void willHaveCorrectImplementedEquals() {
        DefaultAddressEntity base = defaultAddressEntityToTest;
        base.setAddressLine1("somewhere");
        base.setZipCode(1234);
        base.setCountry("NO");
        base.setCity("some city");
        base.setAddressLine2("somewhere else");
        base.setAddressLine3("way out there");

        DefaultAddressEntity equal = new DefaultAddressEntity(defaultAddressEntityToTest);

        DefaultAddressEntity notEqual = new DefaultAddressEntity();
        notEqual.setAddressLine1("somewhere else");
        notEqual.setZipCode(5678);
        notEqual.setCountry("SE");
        notEqual.setCity("some other city");
        notEqual.setAddressLine2("some place");
        notEqual.setAddressLine3("in the distance");

        assertAll(
                () -> assertThat(base).as("%s is equal to %s", base, equal).isEqualTo(equal),
                () -> assertThat(base).as("%s is not equal to %s", base, notEqual).isNotEqualTo(notEqual),
                () -> assertThat(base).as("%s is not equal to %s").isNotEqualTo(null),
                () -> assertThat(base).as("%s is equal to %s").isEqualTo(base),
                () -> assertThat(base).as("base is not same instance as equal").isNotSameAs(equal)
        );
    }
}
