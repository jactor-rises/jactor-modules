package com.github.jactorrises.model.domain.user;

import com.github.jactorrises.model.domain.person.PersonBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.jactorrises.model.domain.address.AddressDomain.anAddress;
import static com.github.jactorrises.model.domain.person.PersonDomain.aPerson;
import static com.github.jactorrises.model.domain.user.UserDomain.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

@DisplayName("The UserBuilder")
class UserBuilderTest {

    @DisplayName("should not build an instance without a user name")
    @Test void willNotBuildUserDomainWithoutUserName() {
        assertThatIllegalStateException().isThrownBy(() -> aUser().with(aValidPerson()).build())
                .withMessageContaining("user name").withMessageContaining("must be present");
    }

    @DisplayName("should not build an instance without a person")
    @Test void willNotBuildUserDomainWithoutPerson() {
        assertThatIllegalStateException().isThrownBy(() -> aUser().withUserName("some user").build())
                .withMessageContaining("person").withMessageContaining("must be present");
    }

    @DisplayName("should build an instance when all required fields are set")
    @Test void willBuildUserDomainWithAllRequiredProperties() {
        assertThat(aUser().withUserName("some user").with(aValidPerson()).build()).isNotNull();
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
