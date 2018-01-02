package com.github.jactorrises.model.service;

import com.github.jactorrises.client.datatype.EmailAddress;
import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.model.JactorModel;
import com.github.jactorrises.model.domain.address.AddressBuilder;
import com.github.jactorrises.model.domain.guestbook.GuestBookDomain;
import com.github.jactorrises.model.domain.guestbook.GuestBookEntryDomain;
import com.github.jactorrises.model.domain.person.PersonDomain;
import com.github.jactorrises.model.domain.user.UserDomain;
import com.github.jactorrises.persistence.PersistenceBeans;
import com.github.jactorrises.persistence.PersistenceOrmApplication;
import org.assertj.core.api.SoftAssertions;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.Serializable;
import java.util.Optional;

import static com.github.jactorrises.model.domain.address.AddressDomain.anAddress;
import static com.github.jactorrises.model.domain.guestbook.GuestBookDomain.aGuestBook;
import static com.github.jactorrises.model.domain.guestbook.GuestBookEntryDomain.aGuestBookEntry;
import static com.github.jactorrises.model.domain.person.PersonDomain.aPerson;
import static com.github.jactorrises.model.domain.user.UserDomain.aUser;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PersistenceOrmApplication.class)
@Import({JactorModel.class, PersistenceBeans.class})
@Ignore("#176: rewrite using rest from persistence-orm... ")
public class DomainServicesIntegrationTest {

    @Autowired private GuestBookDomainService guestBookDomainService;

    @Autowired private UserDomainService userDomainService;

    @Test public void shouldSaveUserDomain() {
        userDomainService.saveOrUpdateUser(
                aUser().withUserName("titten")
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

        Optional<UserDomain> possibleUser = userDomainService.find(new UserName("titten"));

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(possibleUser).isPresent();
            UserDomain userDomain = possibleUser.orElse(aUser().build());
            softly.assertThat(userDomain.getEmailAddress()).as("user.emailAddress").isEqualTo(new EmailAddress("jactor", "rises"));
            softly.assertThat(userDomain.getPerson().getDescription()).as("user.description").isEqualTo("description");
        });
    }

    @Test public void willSaveGuestbookWithRelations() {
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
                .withUserName("titten")
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


    @Test public void willSaveGuestBookEntryWithRelations() {
        UserDomain userDomain = aUser().withUserName("titten")
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