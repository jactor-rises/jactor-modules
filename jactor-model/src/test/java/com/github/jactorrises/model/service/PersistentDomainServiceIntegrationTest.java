package com.github.jactorrises.model.service;

import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.model.JactorModule;
import com.github.jactorrises.model.domain.address.AddressBuilder;
import com.github.jactorrises.model.domain.guestbook.GuestBookDomain;
import com.github.jactorrises.model.domain.guestbook.GuestBookEntryDomain;
import com.github.jactorrises.model.domain.person.PersonDomain;
import com.github.jactorrises.model.domain.user.UserDomain;
import com.github.jactorrises.model.service.PersistentDomainService.GuestBookCriterion;
import com.github.jactorrises.model.service.PersistentDomainService.GuestBookEntryCriterion;
import org.assertj.core.api.SoftAssertions;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.github.jactorrises.model.domain.address.AddressDomain.anAddress;
import static com.github.jactorrises.model.domain.guestbook.GuestBookDomain.aGuestBook;
import static com.github.jactorrises.model.domain.guestbook.GuestBookEntryDomain.aGuestBookEntry;
import static com.github.jactorrises.model.domain.person.PersonDomain.aPerson;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = JactorModule.class)
@Transactional
public class PersistentDomainServiceIntegrationTest {

    @Autowired
    private PersistentDomainService persistentDomainService;

    @Autowired
    private SessionFactory sessionFactory;

    @Test public void shouldSaveUserDomain() {
        persistentDomainService.saveOrUpdate(
                UserDomain.aUser().withUserName("titten")
                        .withEmailAddress("jactor@rises")
                        .with(aPerson().withDescription("description")
                                .with(anAddress().withAddressLine1("the streets")
                                        .withCity("Dirdal")
                                        .withCountry("NO")
                                        .withZipCode(1234)
                                )
                        ).build()
        );

        ensureDbCreation();

        Optional<UserDomain> possibleUser = persistentDomainService.findUser(new UserName("titten"));

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(possibleUser).isPresent();
            UserDomain userDomain = possibleUser.get();
//            softly.assertThat(userDomain.getEmailAddress()).as("user.emailAddress").isEqualTo(new EmailAddress("jactor", "rises")); todo fix: should work after story #131 is resolved
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
                .with(address)
                .build();
        UserDomain user = UserDomain.aUser()
                .withUserName("titten")
                .withEmailAddress("jactor@rises")
                .with(person)
                .build();

        Long id = persistentDomainService.saveOrUpdateGuestBook(aGuestBook().withTitle("my guest book").with(user).build()).getEntity().getId();

        ensureDbCreation();

        GuestBookDomain guestBook = persistentDomainService.findUnique(new GuestBookCriterion().with(id));

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(guestBook.getTitle()).isEqualTo("my guest book");
            softly.assertThat(guestBook.getUser().getId()).isEqualTo(user.getId());
        });
    }


    @Test public void willSaveGuestBookEntryWithRelations() {
        UserDomain userDomain = UserDomain.aUser().withUserName("titten")
                .withEmailAddress("jactor@rises")
                .with(aPerson().withDescription("description")
                        .with(anAddress().withAddressLine1("the streets")
                                .withCity("Dirdal")
                                .withCountry("NO")
                                .withZipCode(1234)
                        )
                ).build();

        GuestBookDomain guestBookDomain = aGuestBook().with(userDomain).withTitle("my guest book").build();
        GuestBookEntryDomain guestBookEntryDomain = aGuestBookEntry().with(guestBookDomain).withEntry("svada", "lada").build();

        Long id = persistentDomainService.saveOrUpdateGuestBookEntry(guestBookEntryDomain).getId();

        ensureDbCreation();

        GuestBookEntryDomain guestBookEntry = persistentDomainService.findUnique(new GuestBookEntryCriterion().with(id));

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(guestBookEntry.getGuestBook().getTitle()).as("guest book.title").isEqualTo("my guest book");
            softly.assertThat(guestBookEntry.getCreatedTime()).as("entry.createdTime").isNotNull();
            softly.assertThat(guestBookEntry.getCreatorName()).as("entry.creatorName").isEqualTo(new Name("lada"));
            softly.assertThat(guestBookEntry.getEntry()).as("entry.entry").isEqualTo("svada");
        });
    }

    private void ensureDbCreation() {
        sessionFactory.getCurrentSession().flush();
        sessionFactory.getCurrentSession().clear();
    }
}