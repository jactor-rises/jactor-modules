package com.github.jactorrises.model.business.domain.builder;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("A DomianBuilder")
class DomainBuilderTest {

    @DisplayName("should build a domain when the build method is invoked")
    @Test void shouldBuildDomainWhenBuildMethodIsInvoked() {
        assertThat(new TestDomainBuilder(Collections.emptyList()).build()).isNotNull();
    }

    @DisplayName("should throw exception when illegal build is done")
    @Test void shouldValidateDomainWhenBuildMethodIsInvokedg() {
        IllegalArgumentException illegalArgumentException = assertThrows(
                IllegalArgumentException.class, () -> new TestDomainBuilder(
                        initAnListWith("example of invalid field")
                ).build()
        );

        assertThat(illegalArgumentException.getMessage()).isEqualTo("example of invalid field");
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

    private class TestDomainBuilder extends DomainBuilder<Bean> {

        TestDomainBuilder(List<FieldValidator.ValidateField<Bean>> validFields) {
            super(validFields);
        }

        @Override protected Bean addhRequiredFields() {
            return new Bean();
        }
    }
}
