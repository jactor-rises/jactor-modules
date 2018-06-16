package com.github.jactor.rises.model.facade;

import com.github.jactor.rises.client.datatype.EmailAddress;
import com.github.jactor.rises.client.datatype.Name;
import com.github.jactor.rises.client.datatype.Username;
import com.github.jactor.rises.client.domain.User;
import com.github.jactor.rises.io.ctx.JactorIo;
import com.github.jactor.rises.model.domain.address.AddressBuilder;
import com.github.jactor.rises.model.domain.guestbook.GuestBookDomain;
import com.github.jactor.rises.model.domain.guestbook.GuestBookEntryDomain;
import com.github.jactor.rises.model.domain.person.PersonDomain;
import com.github.jactor.rises.model.domain.user.UserDomain;
import com.github.jactor.rises.model.service.GuestBookDomainService;
import com.github.jactor.rises.model.service.UserDomainService;
import com.github.jactor.rises.test.extension.validate.SuppressValidInstanceExtension;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.github.jactor.rises.model.domain.address.AddressDomain.anAddress;
import static com.github.jactor.rises.model.domain.guestbook.GuestBookDomain.aGuestBook;
import static com.github.jactor.rises.model.domain.guestbook.GuestBookEntryDomain.aGuestBookEntry;
import static com.github.jactor.rises.model.domain.person.PersonDomain.aPerson;
import static com.github.jactor.rises.model.domain.user.UserDomain.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("The DomainServicesIntegrationTest")
@ExtendWith(SpringExtension.class)
@ExtendWith(SuppressValidInstanceExtension.class)
@ContextConfiguration(classes = {JactorFacade.class, JactorIo.class})
@Disabled("#199: need to create a @BeforeEach method that verifies that JactorPersistence is running")
class DomainServicesIntegrationTest {

    private @Autowired GuestBookDomainService guestBookDomainService;
    private @Autowired UserDomainService userDomainService;

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

    @DisplayName("should save user domain")
    @Test void shouldSaveUserDomain() {
        Username username = userDomainService.saveOrUpdate(
                aUser()
                        .withUsername(createUnique("titten"))
                        .withEmailAddress("jactor@rises")
                        .with(aPerson()
                                .withDescription("description")
                                .withSurname("jacobsen")
                                .with(anAddress()
                                        .withAddressLine1("the streets")
                                        .withCity("Dirdal")
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

    @DisplayName("should save guest book with relations")
    @Test void shouldSaveGuestBookWithRelations() {
        AddressBuilder address = anAddress()
                .withAddressLine1("the streets")
                .withCity("Dirdal")
                .withCountry("NO")
                .withZipCode(1234);
        PersonDomain person = aPerson()
                .withDescription("description")
                .withSurname("jacobsen")
                .with(address)
                .build();
        UserDomain user = aUser()
                .withUsername(createUnique("titten"))
                .withEmailAddress("jactor@rises")
                .with(person)
                .build();

        Serializable id = guestBookDomainService.saveOrUpdate(
                aGuestBook().withTitle("my guest book").with(user).build()
        ).getDto().getId();

        GuestBookDomain guestBook = guestBookDomainService.find(id).orElse(aGuestBook().build());

        assertAll(
                () -> assertThat(guestBook.getTitle()).as("guest book title").isEqualTo("my guest book"),
                () -> assertThat(guestBook.getUser().getUsername()).as("guestBook.user.username")
                        .isEqualTo(user.getUsername())
        );
    }

    @DisplayName("should save guest book entry with relations")
    @Test void shouldSaveGuestBookEntryWithRelations() {
        UserDomain userDomain = aUser()
                .withUsername(createUnique("titten"))
                .withEmailAddress("jactor@rises")
                .with(aPerson()
                        .withDescription("description")
                        .withSurname("nevland")
                        .with(anAddress().withAddressLine1("the streets")
                                .withCity("Ålgård")
                                .withCountry("NO")
                                .withZipCode(1234)
                        )
                ).build();

        GuestBookDomain guestBookDomain = aGuestBook().with(userDomain).withTitle("my guest book").build();
        GuestBookEntryDomain guestBookEntryDomain = aGuestBookEntry().with(guestBookDomain).withEntry("svada", "lada").build();

        Serializable id = guestBookDomainService.saveOrUpdateEntry(guestBookEntryDomain).getId();

        GuestBookEntryDomain guestBookEntry = guestBookDomainService.findEntry(id).orElse(aGuestBookEntry().build());

        assertAll(
                () -> assertThat(guestBookEntry.getGuestBook().getTitle()).as("guest book.title").isEqualTo("my guest book"),
                () -> assertThat(guestBookEntry.getCreatedTime()).as("entry.createdTime").isNotNull(),
                () -> assertThat(guestBookEntry.getCreatorName()).as("entry.creatorName").isEqualTo(new Name("lada")),
                () -> assertThat(guestBookEntry.getEntry()).as("entry.entry").isEqualTo("svada")
        );
    }

    private String createUnique(String username) {
        return username + "@" + LocalDateTime.now();
    }

}
