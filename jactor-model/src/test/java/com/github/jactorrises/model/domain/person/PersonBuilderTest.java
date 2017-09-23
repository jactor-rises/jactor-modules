package com.github.jactorrises.model.domain.person;

import com.github.jactorrises.model.domain.address.AddressBuilder;
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
        assertThatIllegalArgumentException().isThrownBy(() -> aPerson().withSurname("jacobsen").build())
                .withMessage(PersonBuilder.AN_ADDRESS_MUST_BE_PRESENT);
    }

    @DisplayName("should build an instance when all required fields are set")
    @Test void shouldNotBuildAnInstanceWitoutSurname() {
        assertThatIllegalArgumentException().isThrownBy(() -> aPerson().with(aValidAddressBuilder()).build())
                .withMessage(PersonBuilder.A_PERSON_NEEDS_A_SURNAME);
    }

    @DisplayName("should build an instance when all required fields are set")
    @Test void willBuildPersonDomainWhenAllRequiredFieldsAreSet() {
        PersonDomain person = aPerson()
                .with(aValidAddressBuilder())
                .withSurname("nevland")
                .withDescription("description field only for coverage")
                .withFirstName("anne (for coverage only")
                .withLocale("no") // for coverage only
                .build();

        assertThat(person).isNotNull();
    }

    private AddressBuilder aValidAddressBuilder() {
        return anAddress()
                .withAddressLine1("somewhere")
                .withZipCode(1234)
                .withCountry("NO");
    }
}
