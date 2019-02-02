package com.gitlab.jactor.persistence.builder;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("An instance of the AbstractBuilder")
class AbstractBuilderTest {

    @DisplayName("should not fail when build is containing required fields")
    @Test void shouldNotFailWhenBuildIsContainingRequiredFields() {
        @SuppressWarnings("unchecked") ValidInstance<Object> validInstanceMock = mock(ValidInstance.class);

        (new AbstractBuilder<>(validInstanceMock) {
            @Override protected Object buildBean() {
                return new Object();
            }
        }).build();

        verify(validInstanceMock).validate(notNull(), notNull());
    }

    @DisplayName("should fail when build is missing required fields")
    @Test void shouldFailWhenBuildIsMissingRequiredFields() {
        @SuppressWarnings("unchecked") ValidInstance<Object> validInstanceMock = mock(ValidInstance.class);

        when(validInstanceMock.validate(any(Object.class), any(MissingFields.class))).thenReturn(missingFields().presentWhenFieldsAreMissing());

        assertThatIllegalStateException().isThrownBy(
                () -> (new AbstractBuilder<>(validInstanceMock) {
                    @Override protected Object buildBean() {
                        return new Object();
                    }
                }).build()
        )
                .withMessageContaining("Missing fields on Bean:")
                .withMessageContaining("someStringField")
                .withMessageContaining("someObjectField");
    }

    private MissingFields missingFields() {
        MissingFields missingFields = new MissingFields();
        missingFields.addInvalidFieldWhenNoValue("Bean", "someStringField", "");
        missingFields.addInvalidFieldWhenNoValue("Bean", "someObjectField", null);

        return missingFields;
    }
}
