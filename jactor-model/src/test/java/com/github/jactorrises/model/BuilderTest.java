package com.github.jactorrises.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DisplayName("A Builder")
class BuilderTest {

    @DisplayName("should build bean when the build method is invoked")
    @Test void shouldBuildDomainWhenBuildMethodIsInvoked() {
        assertThat(new TestBuilder(Collections.emptyList()).build()).isInstanceOf(Bean.class);
    }

    @DisplayName("should throw an exception when illegal build is done")
    @Test void shouldValidateDomainWhenBuildMethodIsInvokedg() {
        assertThatIllegalArgumentException().isThrownBy(() -> new TestBuilder(initAnListWith("example of invalid field")).build())
                .withMessage("example of invalid field");
    }

    @SuppressWarnings("unchecked") private List<FieldValidator.ValidateField<Bean>> initAnListWith(
            @SuppressWarnings("SameParameterValue") String invalidMessage
    ) {
        return Collections.singletonList(
                (domain) -> invalidMessage == null ? Optional.empty() : Optional.of(invalidMessage)
        );
    }

    private class Bean {
    }

    private class TestBuilder extends Builder<Bean> {

        TestBuilder(List<FieldValidator.ValidateField<Bean>> validFields) {
            super(validFields);
        }

        @Override protected Bean buildBeforeValidation() {
            return new Bean();
        }
    }
}
