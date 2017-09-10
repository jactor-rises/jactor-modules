package com.github.jactorrises.model.internal.service;

import com.github.jactorrises.client.datatype.Description;
import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.model.internal.JactorModule;
import com.github.jactorrises.model.internal.domain.address.AddressDomainBuilder;
import com.github.jactorrises.model.internal.domain.guestbook.GuestBookDomain;
import com.github.jactorrises.model.internal.domain.person.PersonDomain;
import com.github.jactorrises.model.internal.domain.user.UserDomain;
import com.github.jactorrises.model.internal.service.PersistentDomainService.GuestBookCriterion;
import org.assertj.core.api.SoftAssertions;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.github.jactorrises.model.internal.domain.address.AddressDomain.anAddress;
import static com.github.jactorrises.model.internal.domain.guestbook.GuestBookDomain.aGuestBook;
import static com.github.jactorrises.model.internal.domain.person.PersonDomain.aPerson;
import static com.github.jactorrises.model.internal.domain.user.UserDomain.aUser;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = JactorModule.class)
@Transactional
public class PersistentDomainServiceIntegrationTest {

    @Autowired
    private PersistentDomainService persistentDomainService;

    @Autowired
    private SessionFactory sessionFactory;

    @Test
    public void shouldSaveUserDomain() {
        persistentDomainService.saveOrUpdate(
                aUser().withUserName("titten")
                        .withPassword("demo")
                        .withEmailAddress("jactor@rises")
                        .with(aPerson().withDescription("description")
                                .with(anAddress().withAddressLine1("the streets")
                                        .withCity("Dirdal")
                                        .withCountry("NO")
                                        .withZipCode(1234)
                                )
                        ).build()
        );

        emptySession();

        Optional<UserDomain> possibleUser = persistentDomainService.findUser(new UserName("titten"));

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(possibleUser).isPresent();
            UserDomain userDomain = possibleUser.get();
//            softly.assertThat(userDomain.getEmailAddress()).as("user.emailAddress").isEqualTo(new EmailAddress("jactor", "rises")); todo fix: should work after story #131 is resolved
            softly.assertThat(userDomain.getPassword()).as("user.password").isEqualTo("demo");
            softly.assertThat(userDomain.getPerson().getDescription()).as("user.description").isEqualTo(new Description("description"));
        });

    }

    public void willSaveGuestbookWithItsDependencies() {
        AddressDomainBuilder address = anAddress()
                .withAddressLine1("the streets")
                .withCity("Dirdal")
                .withCountry("NO")
                .withZipCode(1234);
        PersonDomain person = aPerson()
                .withDescription("description")
                .with(address)
                .build();
        UserDomain user = aUser()
                .withUserName("titten")
                .withPassword("demo")
                .withEmailAddress("jactor@rises")
                .with(person)
                .build();

        Long id = persistentDomainService.saveOrUpdateGuestBook(aGuestBook().withTitle("my guest book").with(user).build()).getEntity().getId();

        emptySession();

        GuestBookDomain guestBook = persistentDomainService.findUnique(new GuestBookCriterion().with(id));

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(guestBook.getTitle()).isEqualTo("my guest book");
            softly.assertThat(guestBook.getUser().getId()).isEqualTo(user.getId());
        });
    }

    private void emptySession() {
        sessionFactory.getCurrentSession().flush();
        sessionFactory.getCurrentSession().clear();
    }
}