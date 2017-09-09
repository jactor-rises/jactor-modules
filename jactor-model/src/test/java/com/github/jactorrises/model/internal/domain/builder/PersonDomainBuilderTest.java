package com.github.jactorrises.model.internal.domain.builder;

import com.github.jactorrises.model.internal.domain.PersonDomain;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.jactorrises.model.internal.domain.AddressDomain.anAddress;
import static com.github.jactorrises.model.internal.domain.PersonDomain.aPerson;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Java6Assertions.assertThat;

@DisplayName("The PersonDomainBuilder")
class PersonDomainBuilderTest {

    @DisplayName("should not build an instance without an address")
    @Test void willNotBuildPersonDomainWithoutAnAddress() {
        assertThatIllegalArgumentException().isThrownBy(aPerson()::build).withMessage(PersonDomainBuilder.AN_ADDRESS_MUST_BE_PRESENT);
    }

    @DisplayName("should build an instance when all required fields are set")
    @Test void willBuildPersonDomainWhenAllRequiredFieldsAreSet() throws Exception {
        PersonDomain person = aPerson()
                .with(anAddress()
                        .withAddressLine1As("somewhere")
                        .withZipCodeAs(1234)
                        .withCountryAs("NO")
                ).withDescriptionAs("description field only for coverage")
                .build();

        assertThat(person).isNotNull();
    }
}
