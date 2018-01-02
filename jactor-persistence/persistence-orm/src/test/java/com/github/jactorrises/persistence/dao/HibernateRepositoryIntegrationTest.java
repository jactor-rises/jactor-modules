package com.github.jactorrises.persistence.dao;

import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.client.persistence.dto.UserDto;
import com.github.jactorrises.persistence.PersistenceOrmApplication;
import com.github.jactorrises.persistence.entity.address.AddressEntity;
import com.github.jactorrises.persistence.entity.blog.BlogEntity;
import com.github.jactorrises.persistence.entity.blog.BlogEntryEntity;
import com.github.jactorrises.persistence.entity.guestbook.GuestBookEntity;
import com.github.jactorrises.persistence.entity.guestbook.GuestBookEntryEntity;
import com.github.jactorrises.persistence.entity.person.PersonEntity;
import com.github.jactorrises.persistence.entity.user.UserEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Optional;

import static com.github.jactorrises.persistence.entity.address.AddressEntity.anAddress;
import static com.github.jactorrises.persistence.entity.blog.BlogEntity.aBlog;
import static com.github.jactorrises.persistence.entity.blog.BlogEntryEntity.aBlogEntry;
import static com.github.jactorrises.persistence.entity.guestbook.GuestBookEntity.aGuestBook;
import static com.github.jactorrises.persistence.entity.guestbook.GuestBookEntryEntity.aGuestBookEntry;
import static com.github.jactorrises.persistence.entity.person.PersonEntity.aPerson;
import static com.github.jactorrises.persistence.entity.user.UserEntity.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = PersistenceOrmApplication.class)
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
        int noOfEntities = session().createCriteria(UserEntity.class).list().size();

        AddressEntity AddressEntity = hibernateRepository.saveOrUpdate(
                anAddress()
                        .withAddressLine1("dark alley")
                        .withZipCode(56789)
                        .withCity("big apple")
                        .build()
        );

        PersonEntity personEntity = hibernateRepository.saveOrUpdate(
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
            softly.assertThat(session().createCriteria(UserEntity.class).list()).as("persisted entities").hasSize(noOfEntities + 1);
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

        PersonEntity personEntity = hibernateRepository.saveOrUpdate(
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

        UserEntity userEntity = hibernateRepository.fetch(UserEntity.class, id);

        assertThat(userEntity.getUserName()).as("userName").isEqualTo("jactor");
    }

    @Test
    public void shouldFindGuestBook() {
        int noOfEntities = session().createCriteria(GuestBookEntity.class).list().size();

        AddressEntity AddressEntity = hibernateRepository.saveOrUpdate(
                anAddress()
                        .withAddressLine1("dark alley")
                        .withZipCode(56789)
                        .withCity("big apple")
                        .build()
        );

        PersonEntity personEntity = hibernateRepository.saveOrUpdate(
                aPerson()
                        .with(AddressEntity)
                        .withSurname("jacobsen")
                        .build()
        );

        UserEntity userEntity = hibernateRepository.saveOrUpdate(
                aUser().with(personEntity).withUserName("jactor").build()
        );

        Long id = hibernateRepository.saveOrUpdate(
                aGuestBook()
                        .withTitle("no rest for the wicked")
                        .with(userEntity)
                        .build()
        ).getId();

        assertSoftly(softly -> {
            softly.assertThat(id).as("id").isNotNull();
            softly.assertThat(session().createCriteria(GuestBookEntity.class).list()).as("persisted entities").hasSize(noOfEntities + 1);
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

        PersonEntity personEntity = hibernateRepository.saveOrUpdate(
                aPerson()
                        .with(AddressEntity)
                        .withSurname("jacobsen")
                        .build()
        );

        UserEntity userEntity = hibernateRepository.saveOrUpdate(
                aUser().with(personEntity).withUserName("jactor").build()
        );

        Long id = hibernateRepository.saveOrUpdate(
                aGuestBook()
                        .withTitle("no rest for the wicked")
                        .with(userEntity)
                        .build()
        ).getId();

        session().flush();
        session().clear();

        GuestBookEntity guestBookEntity = hibernateRepository.fetch(GuestBookEntity.class, id);

        assertSoftly(softly -> {
            softly.assertThat(guestBookEntity.getTitle()).as("title").isEqualTo("no rest for the wicked");
            softly.assertThat(guestBookEntity.getUser()).as("user").isEqualTo(userEntity);
        });
    }

    @Test
    public void shouldFindGuestBookEntry() {
        int noOfEntities = session().createCriteria(GuestBookEntryEntity.class).list().size();

        AddressEntity AddressEntity = hibernateRepository.saveOrUpdate(
                anAddress()
                        .withAddressLine1("dark alley")
                        .withZipCode(56789)
                        .withCity("big apple")
                        .build()
        );

        PersonEntity personEntity = hibernateRepository.saveOrUpdate(
                aPerson()
                        .with(AddressEntity)
                        .withSurname("jacobsen")
                        .build()
        );

        UserEntity userEntity = hibernateRepository.saveOrUpdate(
                aUser().with(personEntity).withUserName("jactor").build()
        );

        GuestBookEntity guestBookEntity = hibernateRepository.saveOrUpdate(
                aGuestBook()
                        .withTitle("no rest for the wicked")
                        .with(userEntity)
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
            softly.assertThat(session().createCriteria(GuestBookEntryEntity.class).list()).as("persisted entities").hasSize(noOfEntities + 1);
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

        PersonEntity personEntity = hibernateRepository.saveOrUpdate(
                aPerson()
                        .with(AddressEntity)
                        .withSurname("jacobsen")
                        .build()
        );

        UserEntity userEntity = hibernateRepository.saveOrUpdate(
                aUser().with(personEntity).withUserName("jactor").build()
        );

        GuestBookEntity guestBookEntity = hibernateRepository.saveOrUpdate(
                aGuestBook()
                        .withTitle("no rest for the wicked")
                        .with(userEntity)
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

        GuestBookEntryEntity guestBookEntryEntity = hibernateRepository.fetch(GuestBookEntryEntity.class, id);

        assertSoftly(softly -> {
            softly.assertThat(guestBookEntryEntity.getGuestBook()).as("guest book").isEqualTo(guestBookEntity);
            softly.assertThat(guestBookEntryEntity.getEntry()).as("entry").isEqualTo("hi. long time no see");
            softly.assertThat(guestBookEntryEntity.getCreatorName()).as("creator name").isEqualTo("mate");
        });
    }

    @Test
    public void shouldFindGuestBlog() {
        int noOfEntities = session().createCriteria(BlogEntity.class).list().size();

        AddressEntity AddressEntity = hibernateRepository.saveOrUpdate(
                anAddress()
                        .withAddressLine1("dark alley")
                        .withZipCode(56789)
                        .withCity("big apple")
                        .build()
        );

        PersonEntity personEntity = hibernateRepository.saveOrUpdate(
                aPerson()
                        .with(AddressEntity)
                        .withSurname("jacobsen")
                        .build()
        );

        UserEntity userEntity = hibernateRepository.saveOrUpdate(
                aUser().with(personEntity).withUserName("jactor").build()
        );

        Long id = hibernateRepository.saveOrUpdate(
                aBlog()
                        .withTitle("no rest for the wicked")
                        .with(userEntity)
                        .build()
        ).getId();

        assertSoftly(softly -> {
            softly.assertThat(id).as("id").isNotNull();
            softly.assertThat(session().createCriteria(BlogEntity.class).list()).as("persisted entities").hasSize(noOfEntities + 1);
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

        PersonEntity personEntity = hibernateRepository.saveOrUpdate(
                aPerson()
                        .with(AddressEntity)
                        .withSurname("jacobsen")
                        .build()
        );

        UserEntity userEntity = hibernateRepository.saveOrUpdate(
                aUser().with(personEntity).withUserName("jactor").build()
        );

        Long id = hibernateRepository.saveOrUpdate(
                aBlog()
                        .withTitle("no rest for the wicked")
                        .with(userEntity)
                        .build()
        ).getId();

        session().flush();
        session().clear();

        BlogEntity blogEntity = hibernateRepository.fetch(BlogEntity.class, id);

        assertSoftly(softly -> {
            softly.assertThat(blogEntity.getTitle()).as("title").isEqualTo("no rest for the wicked");
            softly.assertThat(blogEntity.getUser()).as("user").isEqualTo(userEntity);
        });
    }

    @Test
    public void shouldFindBlogEntry() {
        int noOfEntities = session().createCriteria(BlogEntryEntity.class).list().size();

        AddressEntity AddressEntity = hibernateRepository.saveOrUpdate(
                anAddress()
                        .withAddressLine1("dark alley")
                        .withZipCode(56789)
                        .withCity("big apple")
                        .build()
        );

        PersonEntity personEntity = hibernateRepository.saveOrUpdate(
                aPerson()
                        .with(AddressEntity)
                        .withSurname("jacobsen")
                        .build()
        );

        UserEntity userEntity = hibernateRepository.saveOrUpdate(
                aUser().with(personEntity).withUserName("jactor").build()
        );

        BlogEntity blogEntity = hibernateRepository.saveOrUpdate(
                aBlog()
                        .withTitle("no rest for the wicked")
                        .with(userEntity)
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
            softly.assertThat(session().createCriteria(BlogEntryEntity.class).list()).as("persisted entities").hasSize(noOfEntities + 1);
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

        PersonEntity personEntity = hibernateRepository.saveOrUpdate(
                aPerson()
                        .with(AddressEntity)
                        .withSurname("jacobsen")
                        .build()
        );

        UserEntity userEntity = hibernateRepository.saveOrUpdate(
                aUser().with(personEntity).withUserName("jactor").build()
        );

        BlogEntity blogEntity = hibernateRepository.saveOrUpdate(
                aBlog()
                        .withTitle("no rest for the wicked")
                        .with(userEntity)
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

        BlogEntryEntity blogEntryEntity = hibernateRepository.fetch(BlogEntryEntity.class, id);

        assertSoftly(softly -> {
            softly.assertThat(blogEntryEntity.getBlog()).as("blog").isEqualTo(blogEntity);
            softly.assertThat(blogEntryEntity.getEntry()).as("entry").isEqualTo("i do not sleep");
            softly.assertThat(blogEntryEntity.getCreatorName()).as("creator name").isEqualTo("me");
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

        PersonEntity personEntity = hibernateRepository.saveOrUpdate(
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
