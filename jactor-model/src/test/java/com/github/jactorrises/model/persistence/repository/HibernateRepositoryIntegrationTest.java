package com.github.jactorrises.model.persistence.repository;

import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.model.JactorModel;
import com.github.jactorrises.model.persistence.entity.address.AddressOrm;
import com.github.jactorrises.model.persistence.entity.blog.BlogEntryOrm;
import com.github.jactorrises.model.persistence.entity.blog.BlogOrm;
import com.github.jactorrises.model.persistence.entity.guestbook.GuestBookEntity;
import com.github.jactorrises.model.persistence.entity.guestbook.GuestBookEntryEntity;
import com.github.jactorrises.model.persistence.entity.person.PersonEntity;
import com.github.jactorrises.model.persistence.entity.user.UserEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Optional;

import static com.github.jactorrises.model.persistence.entity.address.AddressEntityBuilder.anAddress;
import static com.github.jactorrises.model.persistence.entity.blog.BlogEntryEntityBuilder.aBlogEntry;
import static com.github.jactorrises.model.persistence.entity.blog.BlogEntityBuilder.aBlog;
import static com.github.jactorrises.model.persistence.entity.guestbook.GuestBookEntity.aGuestBook;
import static com.github.jactorrises.model.persistence.entity.guestbook.GuestBookEntryEntity.aGuestBookEntry;
import static com.github.jactorrises.model.persistence.entity.person.PersonEntity.aPerson;
import static com.github.jactorrises.model.persistence.entity.user.UserEntity.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = JactorModel.class)
@Transactional
public class HibernateRepositoryIntegrationTest {

    @Autowired
    private HibernateRepository hibernateRepository;

    @Autowired
    private SessionFactory sessionFactory;

    @Test
    public void shouldFindAddress() {
        int noOfEntities = session().createCriteria(AddressOrm.class).list().size();

        Long id = hibernateRepository.saveOrUpdate(
                anAddress()
                        .withAddressLine1("living on the edge")
                        .withZipCode(1234)
                        .withCity("metropolis")
                        .build()
        ).getId();

        assertSoftly(softly -> {
            softly.assertThat(id).as("id").isNotNull();
            softly.assertThat(session().createCriteria(AddressOrm.class).list()).as("persisted entities").hasSize(noOfEntities + 1);
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

        AddressOrm AddressOrm = hibernateRepository.load(AddressOrm.class, id);

        assertSoftly(softly -> {
            softly.assertThat(AddressOrm.getAddressLine1()).as("addressLine1").isEqualTo("living on the edge");
            softly.assertThat(AddressOrm.getZipCode()).as("zipCode").isEqualTo(1234);
            softly.assertThat(AddressOrm.getCity()).as("city").isEqualTo("metropolis");
        });
    }

    @Test
    public void shouldFindUser() {
        int noOfEntities = session().createCriteria(UserEntity.class).list().size();

        AddressOrm AddressOrm = hibernateRepository.saveOrUpdate(
                anAddress()
                        .withAddressLine1("dark alley")
                        .withZipCode(56789)
                        .withCity("big apple")
                        .build()
        );

        PersonEntity personEntity = hibernateRepository.saveOrUpdate(
                aPerson()
                        .with(AddressOrm)
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
        AddressOrm AddressOrm = hibernateRepository.saveOrUpdate(
                anAddress()
                        .withAddressLine1("dark alley")
                        .withZipCode(56789)
                        .withCity("big apple")
                        .build()
        );

        PersonEntity personEntity = hibernateRepository.saveOrUpdate(
                aPerson()
                        .with(AddressOrm)
                        .withSurname("jacobsen")
                        .build()
        );

        Long id = hibernateRepository.saveOrUpdate(
                aUser().with(personEntity).withUserName("jactor").build()
        ).getId();

        session().flush();
        session().clear();

        UserEntity userEntity = hibernateRepository.load(UserEntity.class, id);

        assertThat(userEntity.getUserName()).as("userName").isEqualTo(new UserName("jactor"));
    }

    @Test
    public void shouldFindGuestBook() {
        int noOfEntities = session().createCriteria(GuestBookEntity.class).list().size();

        AddressOrm AddressOrm = hibernateRepository.saveOrUpdate(
                anAddress()
                        .withAddressLine1("dark alley")
                        .withZipCode(56789)
                        .withCity("big apple")
                        .build()
        );

        PersonEntity personEntity = hibernateRepository.saveOrUpdate(
                aPerson()
                        .with(AddressOrm)
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
        AddressOrm AddressOrm = hibernateRepository.saveOrUpdate(
                anAddress()
                        .withAddressLine1("dark alley")
                        .withZipCode(56789)
                        .withCity("big apple")
                        .build()
        );

        PersonEntity personEntity = hibernateRepository.saveOrUpdate(
                aPerson()
                        .with(AddressOrm)
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

        GuestBookEntity guestBookEntity = hibernateRepository.load(GuestBookEntity.class, id);

        assertSoftly(softly -> {
            softly.assertThat(guestBookEntity.getTitle()).as("title").isEqualTo("no rest for the wicked");
            softly.assertThat(guestBookEntity.getUser()).as("user").isEqualTo(userEntity);
        });
    }

    @Test
    public void shouldFindGuestBookEntry() {
        int noOfEntities = session().createCriteria(GuestBookEntryEntity.class).list().size();

        AddressOrm AddressOrm = hibernateRepository.saveOrUpdate(
                anAddress()
                        .withAddressLine1("dark alley")
                        .withZipCode(56789)
                        .withCity("big apple")
                        .build()
        );

        PersonEntity personEntity = hibernateRepository.saveOrUpdate(
                aPerson()
                        .with(AddressOrm)
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
        AddressOrm AddressOrm = hibernateRepository.saveOrUpdate(
                anAddress()
                        .withAddressLine1("dark alley")
                        .withZipCode(56789)
                        .withCity("big apple")
                        .build()
        );

        PersonEntity personEntity = hibernateRepository.saveOrUpdate(
                aPerson()
                        .with(AddressOrm)
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

        GuestBookEntryEntity guestBookEntryEntity = hibernateRepository.load(GuestBookEntryEntity.class, id);

        assertSoftly(softly -> {
            softly.assertThat(guestBookEntryEntity.getGuestBook()).as("guest book").isEqualTo(guestBookEntity);
            softly.assertThat(guestBookEntryEntity.getEntry()).as("entry").isEqualTo("hi. long time no see");
            softly.assertThat(guestBookEntryEntity.getCreatorName()).as("creator name").isEqualTo(new Name("mate"));
        });
    }

    @Test
    public void shouldFindGuestBlog() {
        int noOfEntities = session().createCriteria(BlogOrm.class).list().size();

        AddressOrm AddressOrm = hibernateRepository.saveOrUpdate(
                anAddress()
                        .withAddressLine1("dark alley")
                        .withZipCode(56789)
                        .withCity("big apple")
                        .build()
        );

        PersonEntity personEntity = hibernateRepository.saveOrUpdate(
                aPerson()
                        .with(AddressOrm)
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
            softly.assertThat(session().createCriteria(BlogOrm.class).list()).as("persisted entities").hasSize(noOfEntities + 1);
        });
    }

    @Test
    public void shouldReadBlogProperties() {
        AddressOrm AddressOrm = hibernateRepository.saveOrUpdate(
                anAddress()
                        .withAddressLine1("dark alley")
                        .withZipCode(56789)
                        .withCity("big apple")
                        .build()
        );

        PersonEntity personEntity = hibernateRepository.saveOrUpdate(
                aPerson()
                        .with(AddressOrm)
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

        BlogOrm blogOrm = hibernateRepository.load(BlogOrm.class, id);

        assertSoftly(softly -> {
            softly.assertThat(blogOrm.getTitle()).as("title").isEqualTo("no rest for the wicked");
            softly.assertThat(blogOrm.getUser()).as("user").isEqualTo(userEntity);
        });
    }

    @Test
    public void shouldFindBlogEntry() {
        int noOfEntities = session().createCriteria(BlogEntryOrm.class).list().size();

        AddressOrm AddressOrm = hibernateRepository.saveOrUpdate(
                anAddress()
                        .withAddressLine1("dark alley")
                        .withZipCode(56789)
                        .withCity("big apple")
                        .build()
        );

        PersonEntity personEntity = hibernateRepository.saveOrUpdate(
                aPerson()
                        .with(AddressOrm)
                        .withSurname("jacobsen")
                        .build()
        );

        UserEntity userEntity = hibernateRepository.saveOrUpdate(
                aUser().with(personEntity).withUserName("jactor").build()
        );

        BlogOrm blogEntity = hibernateRepository.saveOrUpdate(
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
            softly.assertThat(session().createCriteria(BlogEntryOrm.class).list()).as("persisted entities").hasSize(noOfEntities + 1);
        });
    }

    @Test
    public void shouldReadBlogEntryProperties() {
        AddressOrm AddressOrm = hibernateRepository.saveOrUpdate(
                anAddress()
                        .withAddressLine1("dark alley")
                        .withZipCode(56789)
                        .withCity("big apple")
                        .build()
        );

        PersonEntity personEntity = hibernateRepository.saveOrUpdate(
                aPerson()
                        .with(AddressOrm)
                        .withSurname("jacobsen")
                        .build()
        );

        UserEntity userEntity = hibernateRepository.saveOrUpdate(
                aUser().with(personEntity).withUserName("jactor").build()
        );

        BlogOrm blogOrm = hibernateRepository.saveOrUpdate(
                aBlog()
                        .withTitle("no rest for the wicked")
                        .with(userEntity)
                        .build()
        );

        Long id = hibernateRepository.saveOrUpdate(
                aBlogEntry()
                        .with(blogOrm)
                        .withEntry("i do not sleep")
                        .withCreatorName("me")
                        .build()
        ).getId();

        session().flush();
        session().clear();

        BlogEntryOrm blogEntryOrm = hibernateRepository.load(BlogEntryOrm.class, id);

        assertSoftly(softly -> {
            softly.assertThat(blogEntryOrm.getBlog()).as("blog").isEqualTo(blogOrm);
            softly.assertThat(blogEntryOrm.getEntry()).as("entry").isEqualTo("i do not sleep");
            softly.assertThat(blogEntryOrm.getCreatorName()).as("creator name").isEqualTo(new Name("me"));
        });
    }

    @Test
    public void shouldFindDefaultUser() {
        AddressOrm AddressOrm = hibernateRepository.saveOrUpdate(
                anAddress()
                        .withAddressLine1("dark alley")
                        .withZipCode(56789)
                        .withCity("big apple")
                        .build()
        );

        PersonEntity personEntity = hibernateRepository.saveOrUpdate(
                aPerson()
                        .with(AddressOrm)
                        .withSurname("jacobsen")
                        .build()
        );

        hibernateRepository.saveOrUpdate(
                aUser().with(personEntity).withUserName("jactor").build()
        );

        Optional<UserEntity> jactor = hibernateRepository.findUsing(new UserName("jactor"));

        assertThat(jactor.isPresent()).as("jactor is present").isTrue();
    }

    private Session session() {
        return sessionFactory.getCurrentSession();
    }
}
