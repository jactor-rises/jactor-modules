package nu.hjemme.business.domain.builder;

import nu.hjemme.business.domain.PersonDomain;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nu.hjemme.business.domain.AddressDomain.anAddress;
import static nu.hjemme.business.domain.PersonDomain.aPerson;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("The PersonDomainBuilder")
class PersonDomainBuilderTest {

    @DisplayName("should not build an instance without an address")
    @Test void willNotBuildPersonDomainWithoutAnAddress() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> aPerson().build());
        Assertions.assertThat(illegalArgumentException.getMessage()).isEqualTo(PersonDomainBuilder.AN_ADDRESS_MUST_BE_PRESENT);
    }

    @DisplayName("should build an instance when all requered fields are set")
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
