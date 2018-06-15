package com.github.jactor.rises.model.facade;

import com.github.jactor.rises.client.datatype.EmailAddress;
import com.github.jactor.rises.client.datatype.Name;
import com.github.jactor.rises.client.datatype.Username;
import com.github.jactor.rises.io.ctx.JactorIo;
import com.github.jactor.rises.model.domain.address.AddressBuilder;
import com.github.jactor.rises.model.domain.guestbook.GuestBookDomain;
import com.github.jactor.rises.model.domain.guestbook.GuestBookEntryDomain;
import com.github.jactor.rises.model.domain.person.PersonDomain;
import com.github.jactor.rises.model.domain.user.UserDomain;
import com.github.jactor.rises.model.service.GuestBookDomainService;
import com.github.jactor.rises.model.service.UserDomainService;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.Serializable;
import java.util.Optional;

import static com.github.jactor.rises.model.domain.address.AddressDomain.anAddress;
import static com.github.jactor.rises.model.domain.guestbook.GuestBookDomain.aGuestBook;
import static com.github.jactor.rises.model.domain.guestbook.GuestBookEntryDomain.aGuestBookEntry;
import static com.github.jactor.rises.model.domain.person.PersonDomain.aPerson;
import static com.github.jactor.rises.model.domain.user.UserDomain.aUser;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {JactorFacade.class, JactorIo.class})
class DomainServicesIntegrationTest {

    private @Autowired GuestBookDomainService guestBookDomainService;
    private @Autowired UserDomainService userDomainService;


    @Test void shouldSaveUserDomain() {
        userDomainService.saveOrUpdateUser(
                aUser().withUsername("titten")
                        .withEmailAddress("jactor@rises")
                        .with(aPerson()
                                .withDescription("description")
                                .withSurname("jacobsen")
                                .with(anAddress().withAddressLine1("the streets")
                                        .withCity("Dirdal")
                                        .withCountry("NO")
                                        .withZipCode(1234)
                                )
                        ).build()
        );

        Optional<UserDomain> possibleUser = userDomainService.find(new Username("titten"));

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(possibleUser).isPresent();
            UserDomain userDomain = possibleUser.orElse(aUser().build());
            softly.assertThat(userDomain.getEmailAddress()).as("user.emailAddress").isEqualTo(new EmailAddress("jactor", "rises"));
            softly.assertThat(userDomain.getPerson().getDescription()).as("user.description").isEqualTo("description");
        });
    }

    @Test void willSaveGuestbookWithRelations() {
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
                .withUsername("titten")
                .withEmailAddress("jactor@rises")
                .with(person)
                .build();

        Serializable id = guestBookDomainService.saveOrUpdateGuestBook(aGuestBook().withTitle("my guest book").with(user).build()).getDto().getId();

        GuestBookDomain guestBook = guestBookDomainService.fetchGuestBook(id);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(guestBook.getTitle()).isEqualTo("my guest book");
            softly.assertThat(guestBook.getUser().getId()).isEqualTo(user.getId());
        });
    }


    @Test void willSaveGuestBookEntryWithRelations() {
        UserDomain userDomain = aUser().withUsername("titten")
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

        Serializable id = guestBookDomainService.saveOrUpdateGuestBookEntry(guestBookEntryDomain).getId();

        GuestBookEntryDomain guestBookEntry = guestBookDomainService.fetchGuestBookEntry(id);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(guestBookEntry.getGuestBook().getTitle()).as("guest book.title").isEqualTo("my guest book");
            softly.assertThat(guestBookEntry.getCreatedTime()).as("entry.createdTime").isNotNull();
            softly.assertThat(guestBookEntry.getCreatorName()).as("entry.creatorName").isEqualTo(new Name("lada"));
            softly.assertThat(guestBookEntry.getEntry()).as("entry.entry").isEqualTo("svada");
        });
    }
}