package com.github.jactorrises.model.domain.person;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.jactorrises.model.domain.address.AddressDomain.anAddress;
import static com.github.jactorrises.model.domain.person.PersonDomain.aPerson;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Java6Assertions.assertThat;

@DisplayName("The PersonBuilder")
class PersonBuilderTest {

    @DisplayName("should not build an instance without an address")
    @Test void willNotBuildPersonDomainWithoutAnAddress() {
        assertThatIllegalArgumentException().isThrownBy(aPerson()::build).withMessage(PersonBuilder.AN_ADDRESS_MUST_BE_PRESENT);
    }

    @DisplayName("should build an instance when all required fields are set")
    @Test void willBuildPersonDomainWhenAllRequiredFieldsAreSet() throws Exception {
        PersonDomain person = aPerson()
                .with(anAddress()
                        .withAddressLine1("somewhere")
                        .withZipCode(1234)
                        .withCountry("NO")
                ).withDescription("description field only for coverage")
                .build();

        assertThat(person).isNotNull();
    }
}
