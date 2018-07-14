package com.gitlab.jactor.rises.model.domain.person;

import com.gitlab.jactor.rises.model.domain.address.AddressDomain;
import com.gitlab.jactor.rises.test.extension.validate.SuppressValidInstanceExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.gitlab.jactor.rises.model.domain.address.AddressDomain.anAddress;
import static com.gitlab.jactor.rises.model.domain.person.PersonDomain.aPerson;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.assertj.core.api.Java6Assertions.assertThat;

@ExtendWith(SuppressValidInstanceExtension.class)
@DisplayName("A PersonBuilder")
class PersonBuilderTest {

    @BeforeEach
    void suppressValidationForAnAddress() {
        SuppressValidInstanceExtension.suppressFor(AddressDomain.class);
    }

    @DisplayName("should not build an instance without an address")
    @Test void willNotBuildPersonDomainWithoutAnAddress() {
        assertThatIllegalStateException().isThrownBy(() -> aPerson().withSurname("jacobsen").build())
                .withMessageContaining("address").withMessageContaining("has no value");
    }

    @DisplayName("should not build an instance without a surname")
    @Test void shouldNotBuildAnInstanceWitoutSurname() {
        assertThatIllegalStateException().isThrownBy(() -> aPerson().with(anAddress()).build())
                .withMessageContaining("surname").withMessageContaining("has no value");
    }

    @DisplayName("should build an instance when all required fields are set")
    @Test void willBuildPersonDomainWhenAllRequiredFieldsAreSet() {
        assertThat(
                aPerson()
                        .with(anAddress())
                        .withSurname("nevland")
                        .withDescription("description field only for coverage")
                        .withFirstName("anne (for coverage only")
                        .withLocale("no") // for coverage only
                        .build()
        ).isNotNull();
    }
}
