package com.github.jactorrises.persistence.boot.repository;

import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.persistence.boot.Persistence;
import com.github.jactorrises.persistence.boot.entity.address.AddressEntityImpl;
import com.github.jactorrises.persistence.boot.entity.blog.BlogEntityImpl;
import com.github.jactorrises.persistence.boot.entity.guestbook.GuestBookEntityImpl;
import com.github.jactorrises.persistence.boot.entity.guestbook.GuestBookEntryEntityImpl;
import com.github.jactorrises.persistence.boot.entity.user.UserEntityImpl;
import com.github.jactorrises.persistence.client.AddressEntity;
import com.github.jactorrises.persistence.client.BlogEntity;
import com.github.jactorrises.persistence.client.GuestBookEntity;
import com.github.jactorrises.persistence.client.GuestBookEntryEntity;
import com.github.jactorrises.persistence.client.UserEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static com.github.jactorrises.persistence.boot.entity.address.AddressEntityImpl.anAddress;
import static com.github.jactorrises.persistence.boot.entity.blog.BlogEntityImpl.aBlog;
import static com.github.jactorrises.persistence.boot.entity.guestbook.GuestBookEntityImpl.aGuestBook;
import static com.github.jactorrises.persistence.boot.entity.guestbook.GuestBookEntryEntityImpl.aGuestBookEntry;
import static com.github.jactorrises.persistence.boot.entity.user.UserEntityImpl.aUser;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Persistence.class)
@Transactional
public class HibernateRepositoryIT {

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

        AddressEntity addressEntity = hibernateRepository.load(AddressEntityImpl.class, id);

        assertSoftly(softly -> {
            softly.assertThat(addressEntity.getAddressLine1()).as("addressLine1").isEqualTo("living on the edge");
            softly.assertThat(addressEntity.getZipCode()).as("zipCode").isEqualTo(1234);
            softly.assertThat(addressEntity.getCity()).as("city").isEqualTo("metropolis");
        });
    }

    @Test
    public void shouldFindUser() {
        int noOfEntities = session().createCriteria(UserEntity.class).list().size();

        Long id = hibernateRepository.saveOrUpdate(
                aUser()
                        .withUserName("jactor")
                        .withPassword("enter")
                        .build()
        ).getId();

        assertSoftly(softly -> {
            softly.assertThat(id).as("id").isNotNull();
            softly.assertThat(session().createCriteria(UserEntity.class).list()).as("persisted entities").hasSize(noOfEntities + 1);
        });
    }

    @Test
    public void shouldReadUserProperties() {
        Long id = hibernateRepository.saveOrUpdate(
                aUser()
                        .withUserName("jactor")
                        .withPassword("enter")
                        .build()
        ).getId();

        session().flush();
        session().clear();

        UserEntity userEntity = hibernateRepository.load(UserEntityImpl.class, id);

        assertSoftly(softly -> {
            softly.assertThat(userEntity.getUserName()).as("userName").isEqualTo(new UserName("jactor"));
            softly.assertThat(userEntity.getPassword()).as("password").isEqualTo("enter");
        });
    }

    @Test
    public void shouldFindGuestBook() {
        int noOfEntities = session().createCriteria(GuestBookEntity.class).list().size();

        UserEntity userEntity = hibernateRepository.saveOrUpdate(
                aUser()
                        .withUserName("jactor")
                        .withPassword("enter")
                        .build()
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
        UserEntity userEntity = hibernateRepository.saveOrUpdate(
                aUser()
                        .withUserName("jactor")
                        .withPassword("enter")
                        .build()
        );

        Long id = hibernateRepository.saveOrUpdate(
                aGuestBook()
                        .withTitle("no rest for the wicked")
                        .with(userEntity)
                        .build()
        ).getId();

        session().flush();
        session().clear();

        GuestBookEntity guestBookEntity = hibernateRepository.load(GuestBookEntityImpl.class, id);

        assertSoftly(softly -> {
            softly.assertThat(guestBookEntity.getTitle()).as("title").isEqualTo("no rest for the wicked");
            softly.assertThat(guestBookEntity.getUser()).as("user").isEqualTo(userEntity);
        });
    }

    @Test
    public void shouldFindGuestBookEntry() {
        int noOfEntities = session().createCriteria(GuestBookEntryEntity.class).list().size();

        UserEntity userEntity = hibernateRepository.saveOrUpdate(
                aUser()
                        .withUserName("jactor")
                        .withPassword("enter")
                        .build()
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
        UserEntity userEntity = hibernateRepository.saveOrUpdate(
                aUser()
                        .withUserName("jactor")
                        .withPassword("enter")
                        .build()
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

        GuestBookEntryEntity guestBookEntryEntity = hibernateRepository.load(GuestBookEntryEntityImpl.class, id);

        assertSoftly(softly -> {
            softly.assertThat(guestBookEntryEntity.getGuestBook()).as("guest book").isEqualTo(guestBookEntity);
            softly.assertThat(guestBookEntryEntity.getEntry()).as("entry").isEqualTo("hi. long time no see");
            softly.assertThat(guestBookEntryEntity.getCreatorName()).as("creator name").isEqualTo(new Name("mate"));
        });
    }

    @Test
    public void shouldFindGuestBlog() {
        int noOfEntities = session().createCriteria(BlogEntity.class).list().size();

        UserEntity userEntity = hibernateRepository.saveOrUpdate(
                aUser()
                        .withUserName("jactor")
                        .withPassword("enter")
                        .build()
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
        UserEntity userEntity = hibernateRepository.saveOrUpdate(
                aUser()
                        .withUserName("jactor")
                        .withPassword("enter")
                        .build()
        );

        Long id = hibernateRepository.saveOrUpdate(
                aBlog()
                        .withTitle("no rest for the wicked")
                        .with(userEntity)
                        .build()
        ).getId();

        session().flush();
        session().clear();

        BlogEntity blogEntity = hibernateRepository.load(BlogEntityImpl.class, id);

        assertSoftly(softly -> {
            softly.assertThat(blogEntity.getTitle()).as("title").isEqualTo("no rest for the wicked");
            softly.assertThat(blogEntity.getUser()).as("user").isEqualTo(userEntity);
        });
    }

    private Session session() {
        return sessionFactory.getCurrentSession();
    }
}
