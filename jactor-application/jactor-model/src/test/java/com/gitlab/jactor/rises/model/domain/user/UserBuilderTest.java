package com.gitlab.jactor.rises.model.domain.user;

import com.gitlab.jactor.rises.model.domain.person.PersonDomain;
import com.gitlab.jactor.rises.test.extension.validate.SuppressValidInstanceExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.Set;

import static com.gitlab.jactor.rises.model.domain.person.PersonDomain.aPerson;
import static com.gitlab.jactor.rises.model.domain.user.UserDomain.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

@DisplayName("The UserBuilder")
class UserBuilderTest {

    @RegisterExtension SuppressValidInstanceExtension suppressValidation = new SuppressValidInstanceExtension(
            Set.of(PersonDomain.class)
    );

    @DisplayName("should not build an instance without a username")
    @Test void shouldNotBuildUserDomainWithoutUsername() {
        assertThatIllegalStateException().isThrownBy(() -> aUser().with(aPerson()).build())
                .withMessageContaining("username");
    }

    @DisplayName("should not build an instance without a person")
    @Test void shooldNotBuildUserDomainWithoutPerson() {
        assertThatIllegalStateException().isThrownBy(() -> aUser().withUsername("some user").build())
                .withMessageContaining("person");
    }

    @DisplayName("should build an instance when all required fields are set")
    @Test void shouldBuildUserDomainWithAllRequiredProperties() {
        assertThat(aUser().withUsername("some user").with(aPerson()).build()).isNotNull();
    }
}
