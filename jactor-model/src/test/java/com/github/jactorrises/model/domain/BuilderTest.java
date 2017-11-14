package com.github.jactorrises.model.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@DisplayName("A Builder")
class BuilderTest {

    @SuppressWarnings("unchecked") @DisplayName("should validate domain being build")
    @Test void shouldBuildDomainWhenBuildMethodIsInvoked() {
        BuilderTest builderTest = new BuilderTest();
        DomainValidator domainValidatorMock = mock(DomainValidator.class);

        (new Builder(domainValidatorMock) {
            @Override protected BuilderTest buildDomain() {
                return builderTest;
            }
        }).build();

        verify(domainValidatorMock).runOn(builderTest);
    }
}
