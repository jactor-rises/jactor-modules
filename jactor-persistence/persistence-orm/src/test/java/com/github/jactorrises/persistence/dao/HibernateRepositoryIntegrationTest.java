package com.github.jactorrises.persistence.dao;

import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.persistence.JactorPersistence;
import com.github.jactorrises.persistence.client.dto.UserDto;
import com.github.jactorrises.persistence.entity.address.AddressEntity;
import com.github.jactorrises.persistence.entity.blog.BlogEntryOrm;
import com.github.jactorrises.persistence.entity.blog.BlogOrm;
import com.github.jactorrises.persistence.entity.guestbook.GuestBookEntryOrm;
import com.github.jactorrises.persistence.entity.guestbook.GuestBookOrm;
import com.github.jactorrises.persistence.entity.person.PersonOrm;
import com.github.jactorrises.persistence.entity.user.UserOrm;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Optional;

import static com.github.jactorrises.persistence.builder.AddressEntityBuilder.anAddress;
import static com.github.jactorrises.persistence.builder.BlogEntryEntityBuilder.aBlogEntry;
import static com.github.jactorrises.persistence.builder.GuestBookEntityBuilder.aGuestBook;
import static com.github.jactorrises.persistence.builder.GuestBookEntryEntityBuilder.aGuestBookEntry;
import static com.github.jactorrises.persistence.builder.PersonEntityBuilder.aPerson;
import static com.github.jactorrises.persistence.builder.UserEntityBuilder.aUser;
import static com.github.jactorrises.persistence.entity.blog.BlogEntityBuilder.aBlog;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = JactorPersistence.class)
@Transactional
public class HibernateRepositoryIntegrationTest {

    @Autowired
    private HibernateRepository hibernateRepository;

    @Autowired
    private SessionFactory sessionFactory;

    @Test
    public void shouldFindAddress() {
        int noOfEntities = session().createCriteria(AddressEntity.class).list().size();

        Long id = hibernateRepository.saveOrUpdate(
                anAddress()
                        .withAddressLine1("living on the edge")
                        .withZipCode(1234)
                        .withCity("metropolis")
                        .build()
        ).getId();

        assertSoftly(softly -> {
            softly.assertThat(id).as("id").isNotNull();
            softly.assertThat(session().createCriteria(AddressEntity.class).list()).as("persisted entities").hasSize(noOfEntities + 1);
        });
    }

    @Test
    public void shouldReadAddressProperties() {
        Long id = hibernateRepository.saveOrUpdate(
                anAddress()
                        .withAddressLine1("living on the edge")
                        .withZipCode(1234)
                        .withCity("metropolis")
                        .build()
        ).getId();

        session().flush();
        session().clear();

        AddressEntity AddressEntity = hibernateRepository.fetch(AddressEntity.class, id);

        assertSoftly(softly -> {
            softly.assertThat(AddressEntity.getAddressLine1()).as("addressLine1").isEqualTo("living on the edge");
            softly.assertThat(AddressEntity.getZipCode()).as("zipCode").isEqualTo(1234);
            softly.assertThat(AddressEntity.getCity()).as("city").isEqualTo("metropolis");
        });
    }

    @Test
    public void shouldFindUser() {
        int noOfEntities = session().createCriteria(UserOrm.class).list().size();

        AddressEntity AddressEntity = hibernateRepository.saveOrUpdate(
                anAddress()
                        .withAddressLine1("dark alley")
                        .withZipCode(56789)
                        .withCity("big apple")
                        .build()
        );

        PersonOrm personEntity = hibernateRepository.saveOrUpdate(
                aPerson()
                        .with(AddressEntity)
                        .withSurname("jacobsen")
                        .build()
        );

        Long id = hibernateRepository.saveOrUpdate(
                aUser().with(personEntity).withUserName("jactor").build()
        ).getId();

        assertSoftly(softly -> {
            softly.assertThat(id).as("id").isNotNull();
            softly.assertThat(session().createCriteria(UserOrm.class).list()).as("persisted entities").hasSize(noOfEntities + 1);
        });
    }

    @Test
    public void shouldReadUserProperties() {
        AddressEntity AddressEntity = hibernateRepository.saveOrUpdate(
                anAddress()
                        .withAddressLine1("dark alley")
                        .withZipCode(56789)
                        .withCity("big apple")
                        .build()
        );

        PersonOrm personEntity = hibernateRepository.saveOrUpdate(
                aPerson()
                        .with(AddressEntity)
                        .withSurname("jacobsen")
                        .build()
        );

        Long id = hibernateRepository.saveOrUpdate(
                aUser().with(personEntity).withUserName("jactor").build()
        ).getId();

        session().flush();
        session().clear();

        UserOrm userOrm = hibernateRepository.fetch(UserOrm.class, id);

        assertThat(userOrm.getUserName()).as("userName").isEqualTo(new UserName("jactor"));
    }

    @Test
    public void shouldFindGuestBook() {
        int noOfEntities = session().createCriteria(GuestBookOrm.class).list().size();

        AddressEntity AddressEntity = hibernateRepository.saveOrUpdate(
                anAddress()
                        .withAddressLine1("dark alley")
                        .withZipCode(56789)
                        .withCity("big apple")
                        .build()
        );

        PersonOrm personEntity = hibernateRepository.saveOrUpdate(
                aPerson()
                        .with(AddressEntity)
                        .withSurname("jacobsen")
                        .build()
        );

        UserOrm userOrm = hibernateRepository.saveOrUpdate(
                aUser().with(personEntity).withUserName("jactor").build()
        );

        Long id = hibernateRepository.saveOrUpdate(
                aGuestBook()
                        .withTitle("no rest for the wicked")
                        .with(userOrm)
                        .build()
        ).getId();

        assertSoftly(softly -> {
            softly.assertThat(id).as("id").isNotNull();
            softly.assertThat(session().createCriteria(GuestBookOrm.class).list()).as("persisted entities").hasSize(noOfEntities + 1);
        });
    }

    @Test
    public void shouldReadGuestBookProperties() {
        AddressEntity AddressEntity = hibernateRepository.saveOrUpdate(
                anAddress()
                        .withAddressLine1("dark alley")
                        .withZipCode(56789)
                        .withCity("big apple")
                        .build()
        );

        PersonOrm personEntity = hibernateRepository.saveOrUpdate(
                aPerson()
                        .with(AddressEntity)
                        .withSurname("jacobsen")
                        .build()
        );

        UserOrm userOrm = hibernateRepository.saveOrUpdate(
                aUser().with(personEntity).withUserName("jactor").build()
        );

        Long id = hibernateRepository.saveOrUpdate(
                aGuestBook()
                        .withTitle("no rest for the wicked")
                        .with(userOrm)
                        .build()
        ).getId();

        session().flush();
        session().clear();

        GuestBookOrm guestBookEntity = hibernateRepository.fetch(GuestBookOrm.class, id);

        assertSoftly(softly -> {
            softly.assertThat(guestBookEntity.getTitle()).as("title").isEqualTo("no rest for the wicked");
            softly.assertThat(guestBookEntity.getUser()).as("user").isEqualTo(userOrm);
        });
    }

    @Test
    public void shouldFindGuestBookEntry() {
        int noOfEntities = session().createCriteria(GuestBookEntryOrm.class).list().size();

        AddressEntity AddressEntity = hibernateRepository.saveOrUpdate(
                anAddress()
                        .withAddressLine1("dark alley")
                        .withZipCode(56789)
                        .withCity("big apple")
                        .build()
        );

        PersonOrm personEntity = hibernateRepository.saveOrUpdate(
                aPerson()
                        .with(AddressEntity)
                        .withSurname("jacobsen")
                        .build()
        );

        UserOrm userOrm = hibernateRepository.saveOrUpdate(
                aUser().with(personEntity).withUserName("jactor").build()
        );

        GuestBookOrm guestBookEntity = hibernateRepository.saveOrUpdate(
                aGuestBook()
                        .withTitle("no rest for the wicked")
                        .with(userOrm)
                        .build()
        );

        Long id = hibernateRepository.saveOrUpdate(
                aGuestBookEntry()
                        .with(guestBookEntity)
                        .withEntry("hi. long time no see")
                        .withCreatorName("mate")
                        .build()
        ).getId();

        assertSoftly(softly -> {
            softly.assertThat(id).as("id").isNotNull();
            softly.assertThat(session().createCriteria(GuestBookEntryOrm.class).list()).as("persisted entities").hasSize(noOfEntities + 1);
        });
    }

    @Test
    public void shouldReadGuestBookEntryProperties() {
        AddressEntity AddressEntity = hibernateRepository.saveOrUpdate(
                anAddress()
                        .withAddressLine1("dark alley")
                        .withZipCode(56789)
                        .withCity("big apple")
                        .build()
        );

        PersonOrm personEntity = hibernateRepository.saveOrUpdate(
                aPerson()
                        .with(AddressEntity)
                        .withSurname("jacobsen")
                        .build()
        );

        UserOrm userOrm = hibernateRepository.saveOrUpdate(
                aUser().with(personEntity).withUserName("jactor").build()
        );

        GuestBookOrm guestBookEntity = hibernateRepository.saveOrUpdate(
                aGuestBook()
                        .withTitle("no rest for the wicked")
                        .with(userOrm)
                        .build()
        );

        Long id = hibernateRepository.saveOrUpdate(
                aGuestBookEntry()
                        .with(guestBookEntity)
                        .withEntry("hi. long time no see")
                        .withCreatorName("mate")
                        .build()
        ).getId();

        session().flush();
        session().clear();

        GuestBookEntryOrm guestBookEntryOrm = hibernateRepository.fetch(GuestBookEntryOrm.class, id);

        assertSoftly(softly -> {
            softly.assertThat(guestBookEntryOrm.getGuestBook()).as("guest book").isEqualTo(guestBookEntity);
            softly.assertThat(guestBookEntryOrm.getEntry()).as("entry").isEqualTo("hi. long time no see");
            softly.assertThat(guestBookEntryOrm.getCreatorName()).as("creator name").isEqualTo(new Name("mate"));
        });
    }

    @Test
    public void shouldFindGuestBlog() {
        int noOfEntities = session().createCriteria(BlogOrm.class).list().size();

        AddressEntity AddressEntity = hibernateRepository.saveOrUpdate(
                anAddress()
                        .withAddressLine1("dark alley")
                        .withZipCode(56789)
                        .withCity("big apple")
                        .build()
        );

        PersonOrm personEntity = hibernateRepository.saveOrUpdate(
                aPerson()
                        .with(AddressEntity)
                        .withSurname("jacobsen")
                        .build()
        );

        UserOrm userOrm = hibernateRepository.saveOrUpdate(
                aUser().with(personEntity).withUserName("jactor").build()
        );

        Long id = hibernateRepository.saveOrUpdate(
                aBlog()
                        .withTitle("no rest for the wicked")
                        .with(userOrm)
                        .build()
        ).getId();

        assertSoftly(softly -> {
            softly.assertThat(id).as("id").isNotNull();
            softly.assertThat(session().createCriteria(BlogOrm.class).list()).as("persisted entities").hasSize(noOfEntities + 1);
        });
    }

    @Test
    public void shouldReadBlogProperties() {
        AddressEntity AddressEntity = hibernateRepository.saveOrUpdate(
                anAddress()
                        .withAddressLine1("dark alley")
                        .withZipCode(56789)
                        .withCity("big apple")
                        .build()
        );

        PersonOrm personEntity = hibernateRepository.saveOrUpdate(
                aPerson()
                        .with(AddressEntity)
                        .withSurname("jacobsen")
                        .build()
        );

        UserOrm userOrm = hibernateRepository.saveOrUpdate(
                aUser().with(personEntity).withUserName("jactor").build()
        );

        Long id = hibernateRepository.saveOrUpdate(
                aBlog()
                        .withTitle("no rest for the wicked")
                        .with(userOrm)
                        .build()
        ).getId();

        session().flush();
        session().clear();

        BlogOrm blogOrm = hibernateRepository.fetch(BlogOrm.class, id);

        assertSoftly(softly -> {
            softly.assertThat(blogOrm.getTitle()).as("title").isEqualTo("no rest for the wicked");
            softly.assertThat(blogOrm.getUser()).as("user").isEqualTo(userOrm);
        });
    }

    @Test
    public void shouldFindBlogEntry() {
        int noOfEntities = session().createCriteria(BlogEntryOrm.class).list().size();

        AddressEntity AddressEntity = hibernateRepository.saveOrUpdate(
                anAddress()
                        .withAddressLine1("dark alley")
                        .withZipCode(56789)
                        .withCity("big apple")
                        .build()
        );

        PersonOrm personEntity = hibernateRepository.saveOrUpdate(
                aPerson()
                        .with(AddressEntity)
                        .withSurname("jacobsen")
                        .build()
        );

        UserOrm userOrm = hibernateRepository.saveOrUpdate(
                aUser().with(personEntity).withUserName("jactor").build()
        );

        BlogOrm blogEntity = hibernateRepository.saveOrUpdate(
                aBlog()
                        .withTitle("no rest for the wicked")
                        .with(userOrm)
                        .build()
        );

        Long id = hibernateRepository.saveOrUpdate(
                aBlogEntry()
                        .with(blogEntity)
                        .withEntry("i do not sleep")
                        .withCreatorName("me")
                        .build()
        ).getId();

        assertSoftly(softly -> {
            softly.assertThat(id).as("id").isNotNull();
            softly.assertThat(session().createCriteria(BlogEntryOrm.class).list()).as("persisted entities").hasSize(noOfEntities + 1);
        });
    }

    @Test
    public void shouldReadBlogEntryProperties() {
        AddressEntity AddressEntity = hibernateRepository.saveOrUpdate(
                anAddress()
                        .withAddressLine1("dark alley")
                        .withZipCode(56789)
                        .withCity("big apple")
                        .build()
        );

        PersonOrm personEntity = hibernateRepository.saveOrUpdate(
                aPerson()
                        .with(AddressEntity)
                        .withSurname("jacobsen")
                        .build()
        );

        UserOrm userOrm = hibernateRepository.saveOrUpdate(
                aUser().with(personEntity).withUserName("jactor").build()
        );

        BlogOrm blogEntity = hibernateRepository.saveOrUpdate(
                aBlog()
                        .withTitle("no rest for the wicked")
                        .with(userOrm)
                        .build()
        );

        Long id = hibernateRepository.saveOrUpdate(
                aBlogEntry()
                        .with(blogEntity)
                        .withEntry("i do not sleep")
                        .withCreatorName("me")
                        .build()
        ).getId();

        session().flush();
        session().clear();

        BlogEntryOrm blogEntryOrm = hibernateRepository.fetch(BlogEntryOrm.class, id);

        assertSoftly(softly -> {
            softly.assertThat(blogEntryOrm.getBlog()).as("blog").isEqualTo(blogEntity);
            softly.assertThat(blogEntryOrm.getEntry()).as("entry").isEqualTo("i do not sleep");
            softly.assertThat(blogEntryOrm.getCreatorName()).as("creator name").isEqualTo(new Name("me"));
        });
    }

    @Test
    public void shouldFindDefaultUser() {
        AddressEntity AddressEntity = hibernateRepository.saveOrUpdate(
                anAddress()
                        .withAddressLine1("dark alley")
                        .withZipCode(56789)
                        .withCity("big apple")
                        .build()
        );

        PersonOrm personEntity = hibernateRepository.saveOrUpdate(
                aPerson()
                        .with(AddressEntity)
                        .withSurname("jacobsen")
                        .build()
        );

        hibernateRepository.saveOrUpdate(
                aUser().with(personEntity).withUserName("jactor").build()
        );

        Optional<UserDto> jactor = hibernateRepository.findUsing(new UserName("jactor"));

        assertThat(jactor.isPresent()).as("jactor is present").isTrue();
    }

    private Session session() {
        return sessionFactory.getCurrentSession();
    }
}
