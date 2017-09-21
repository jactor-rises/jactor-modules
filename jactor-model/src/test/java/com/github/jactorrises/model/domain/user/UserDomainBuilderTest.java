package com.github.jactorrises.model.domain.user;

import com.github.jactorrises.model.domain.person.PersonDomainBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.jactorrises.model.domain.address.AddressDomain.anAddress;
import static com.github.jactorrises.model.domain.person.PersonDomain.aPerson;
import static com.github.jactorrises.model.domain.user.UserDomain.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DisplayName("The UserDomainBuilder")
class UserDomainBuilderTest {

    @DisplayName("should not build an instance without a user name")
    @Test void willNotBuildUserDomainWithoutUserName() {
        assertThatIllegalArgumentException().isThrownBy(() -> aUser().with(aValidPerson()).withPassword("password").build())
                .withMessage(UserDomainBuilder.THE_USER_NAME_CANNOT_BE_NULL);
    }

    @DisplayName("should not build an instance without a person")
    @Test void willNotBuildUserDomainWithoutPerson() {
        assertThatIllegalArgumentException().isThrownBy(() -> aUser().withUserName("some user").withPassword("password").build())
                .withMessage(UserDomainBuilder.THE_USER_MUST_BE_A_PERSON);
    }

    @DisplayName("should not build an instance without a password")
    @Test void willNotBuildUserDomainWithoutPassword() {
        assertThatIllegalArgumentException().isThrownBy(() -> aUser().withUserName("some user").with(aValidPerson()).build())
                .withMessage(UserDomainBuilder.THE_FIELD_CANNOT_BE_EMPTY);
    }

    @DisplayName("should not build an instance with an empty password")
    @Test void willNotBuildUserDomainWithAnEmptyPassword() {
        assertThatIllegalArgumentException().isThrownBy(() -> aUser().withUserName("some user").with(aValidPerson()).build())
                .withMessage(UserDomainBuilder.THE_FIELD_CANNOT_BE_EMPTY);
    }

    @DisplayName("should build an instance when all required fields are set")
    @Test void willBuildUserDomainWithAllRequiredProperties() {
        assertThat(aUser().withUserName("some user").with(aValidPerson()).withPassword("password").build()).isNotNull();
    }

    private PersonDomainBuilder aValidPerson() {
        return aPerson().with(anAddress()
                .withAddressLine1("out there")
                .withZipCode(666)
                .withCountry("NO")
        );
    }
}
