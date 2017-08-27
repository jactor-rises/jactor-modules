package com.github.jactorrises.business.domain.builder;

import com.github.jactorrises.business.domain.AddressDomain;
import com.github.jactorrises.business.domain.PersonDomain;
import com.github.jactorrises.business.domain.UserDomain;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("A UserDomainBuilder")
class UserDomainBuilderTest {

    @DisplayName("should not build an instance without a user name")
    @Test void willNotBuildUserDomainWithoutUserName() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> UserDomain.aUser().with(aValidPerson()).withPasswordAs("password").build());
        assertThat(illegalArgumentException.getMessage()).isEqualTo(UserDomainBuilder.THE_USER_NAME_CANNOT_BE_NULL);
    }

    @DisplayName("should not build an instance without a person")
    @Test void willNotBuildUserDomainWithoutPerson() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> UserDomain.aUser().withUserNameAs("some user").withPasswordAs("password").build());
        assertThat(illegalArgumentException.getMessage()).isEqualTo(UserDomainBuilder.THE_USER_MUST_BE_A_PERSON);
    }

    @DisplayName("should not build an instance without a password")
    @Test void willNotBuildUserDomainWithoutPassword() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> UserDomain.aUser().withUserNameAs("some user").with(aValidPerson()).build());
        assertThat(illegalArgumentException.getMessage()).isEqualTo(UserDomainBuilder.THE_FIELD_CANNOT_BE_EMPTY);
    }

    @DisplayName("should not build an instance with an empty password")
    @Test void willNotBuildUserDomainWithAnEmptyPassword() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> UserDomain.aUser().withUserNameAs("some user").with(aValidPerson()).build());
        assertThat(illegalArgumentException.getMessage()).isEqualTo(UserDomainBuilder.THE_FIELD_CANNOT_BE_EMPTY);
    }

    @DisplayName("should build an instance when all required fields are set")
    @Test void willBuildUserDomainWithAllRequiredProperties() {
        assertThat(UserDomain.aUser().withUserNameAs("some user").with(aValidPerson()).withPasswordAs("password").build()).isNotNull();
    }

    private PersonDomainBuilder aValidPerson() {
        return PersonDomain.aPerson()
                .with(AddressDomain.anAddress()
                        .withAddressLine1As("out there")
                        .withZipCodeAs(666)
                        .withCountryAs("NO")
                );
    }
}
