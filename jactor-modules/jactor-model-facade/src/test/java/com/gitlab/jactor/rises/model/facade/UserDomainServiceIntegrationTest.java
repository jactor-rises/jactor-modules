package com.gitlab.jactor.rises.model.facade;

import com.gitlab.jactor.rises.client.datatype.EmailAddress;
import com.gitlab.jactor.rises.client.datatype.Name;
import com.gitlab.jactor.rises.client.datatype.Username;
import com.gitlab.jactor.rises.client.domain.User;
import com.gitlab.jactor.rises.io.ctx.JactorIo;
import com.gitlab.jactor.rises.model.domain.user.UserDomain;
import com.gitlab.jactor.rises.model.service.UserDomainService;
import com.gitlab.jactor.rises.test.extension.validate.SuppressValidInstanceExtension;
import com.gitlab.jactor.rises.test.util.SpringBootActuatorUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.gitlab.jactor.rises.model.domain.address.AddressDomain.anAddress;
import static com.gitlab.jactor.rises.model.domain.person.PersonDomain.aPerson;
import static com.gitlab.jactor.rises.model.domain.user.UserDomain.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@DisplayName("The UserDomainService")
@ExtendWith(SpringExtension.class)
@ExtendWith(SuppressValidInstanceExtension.class)
@ContextConfiguration(classes = {JactorFacade.class, JactorIo.class})
class UserDomainServiceIntegrationTest {

    private @Autowired UserDomainService userDomainService;

    @BeforeEach void assumeJactorPersistenceRunning() throws IOException {
        assumeTrue(
                SpringBootActuatorUtil.isServerRunning("http://localhost:1099/jactor-persistence-orm"),
                "Integration test can only run when server is 'UP'"
        );
    }

    @DisplayName("should find user with username jactor")
    @Test void shouldFindJactor() {
        Optional<User> possibleUser = userDomainService.find(new Username("jactor"));

        assertAll(
                () -> assertThat(possibleUser).as("possibleUser").isPresent(),
                () -> {
                    User user = possibleUser.orElse(aUser().build());

                    assertThat(user.getPerson().getFirstName()).as("user.person.firstName").isEqualTo(new Name("Tor Egil"));
                }
        );
    }

    @DisplayName("should find user with username tip")
    @Test void shouldFindTip() {
        Optional<User> possibleUser = userDomainService.find(new Username("tip"));

        assertAll(
                () -> assertThat(possibleUser).as("possibleUser").isPresent(),
                () -> {
                    User user = possibleUser.orElse(aUser().build());

                    assertThat(user.getPerson().getFirstName()).as("user.person.firstName").isEqualTo(new Name("Suthatip"));
                }
        );
    }

    @DisplayName("should find usernames on active users")
    @Test void shouldFindUsernames() {
        List<String> usernames = userDomainService.findAllUsernames();

        assertThat(usernames).contains("jactor", "tip");
    }

    @DisplayName("should save user domain")
    @Test void shouldSaveUserDomain() {
        Username username = userDomainService.saveOrUpdate(
                aUser()
                        .withUsername(createUniqueUsername())
                        .withEmailAddress("jactor@rises")
                        .with(aPerson()
                                .withDescription("description")
                                .withSurname("jacobsen")
                                .with(anAddress()
                                        .withAddressLine1("the streets")
                                        .withCity("Sandefjord")
                                        .withCountry("NO")
                                        .withZipCode(1234)
                                )
                        ).build()
        ).getUsername();

        Optional<UserDomain> possibleUser = userDomainService.find(username);

        assertAll(
                () -> assertThat(possibleUser).as("possibleUser").isPresent(),
                () -> {
                    UserDomain userDomain = possibleUser.orElse(aUser().build());

                    assertAll(
                            () -> assertThat(userDomain.getEmailAddress()).as("user.emailAddress").isEqualTo(new EmailAddress("jactor", "rises")),
                            () -> assertThat(userDomain.getPerson().getDescription()).as("user.description").isEqualTo("description")
                    );
                }
        );
    }

    private String createUniqueUsername() {
        return "username@" + LocalDateTime.now();
    }
}
