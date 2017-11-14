package com.github.jactorrises.model.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@DisplayName("A DomainBuilder")
class DomainBuilderTest {

    @SuppressWarnings("unchecked") @DisplayName("should validate domain being build")
    @Test void shouldBuildDomainWhenBuildMethodIsInvoked() {
        DomainBuilderTest builderTest = new DomainBuilderTest();
        DomainValidator domainValidatorMock = mock(DomainValidator.class);

        (new DomainBuilder(domainValidatorMock) {
            @Override protected DomainBuilderTest buildDomain() {
                return builderTest;
            }
        }).build();

        verify(domainValidatorMock).runOn(builderTest);
    }
}
