package com.gitlab.jactor.rises.model.domain.user;

import com.gitlab.jactor.rises.model.domain.person.PersonBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.gitlab.jactor.rises.model.domain.address.AddressDomain.anAddress;
import static com.gitlab.jactor.rises.model.domain.person.PersonDomain.aPerson;
import static com.gitlab.jactor.rises.model.domain.user.UserDomain.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

@DisplayName("The UserBuilder")
class UserBuilderTest {

    @DisplayName("should not build an instance without a username")
    @Test void shouldNotBuildUserDomainWithoutUsername() {
        assertThatIllegalStateException().isThrownBy(() -> aUser().with(aValidPerson()).build())
                .withMessageContaining("username").withMessageContaining("has no value");
    }

    @DisplayName("should not build an instance without a person")
    @Test void shooldNotBuildUserDomainWithoutPerson() {
        assertThatIllegalStateException().isThrownBy(() -> aUser().withUsername("some user").build())
                .withMessageContaining("person").withMessageContaining("has no value");
    }

    @DisplayName("should build an instance when all required fields are set")
    @Test void shouldBuildUserDomainWithAllRequiredProperties() {
        assertThat(aUser().withUsername("some user").with(aValidPerson()).build()).isNotNull();
    }

    private PersonBuilder aValidPerson() {
        return aPerson()
                .withSurname("nevland")
                .with(
                        anAddress()
                                .withAddressLine1("out there")
                                .withZipCode(666)
                                .withCountry("NO")
                );
    }
}
