package nu.hjemme.client.datatype;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("A description")
class DescriptionTest {

    @DisplayName("should have an implementation of the hashCode method")
    @Test void whenInvokingHashCodeTheImplementationShouldBeCorrect() {
        Description base = new Description(new Name("some item"), "some description");
        Description equal = new Description(new Name("some item"), "some description");
        Description notEqual = new Description(new Name("some other item"), "some other description");

        assertAll(
                () -> assertThat(base.hashCode()).as("(%s).hashCode() is equal to (%s).hashCode()", base, equal).isEqualTo(equal.hashCode()),
                () -> assertThat(base.hashCode()).as("(%s).hashCode() is not equal to (%s).hashCode()", base, notEqual).isNotEqualTo(notEqual.hashCode()),
                () -> assertThat(base.hashCode()).as("(%s).hashCode() is a number with different value", base).isNotEqualTo(0)
        );
    }

    @DisplayName("should have an implementation of the equuals method")
    @Test void whenChecksForEqualityTheImplementationShouldBeCorrect() {
        Description base = new Description(new Name("some item"), "some description");
        Description equal = new Description(new Name("some item"), "some description");
        Description notEqual = new Description(new Name("some other item"), "some other description");


        assertAll(
                () -> assertThat(base).as("%s is equal to %s", base, equal).isEqualTo(equal),
                () -> assertThat(base).as("%s is not equal to %s", base, notEqual).isNotEqualTo(notEqual),
                () -> assertThat(base).as("%s is not equal to %s").isNotEqualTo(null),
                () -> assertThat(base).as("%s is equal to %s").isEqualTo(base),
                () -> assertThat(base).as("base is not same instance as equal").isNotSameAs(equal)
        );
    }

    @DisplayName("will have the toString method implemented")
    @Test void willSpellDescriptionInToString() {
        assertAll(
                () -> assertThat(new Description("some description").toString()).isEqualTo("some description"),
                () -> assertThat(new Description(new Name("a man"), "without a cause").toString()).isEqualTo("Description[Name[a man],without a cause]")
        );
    }
}
