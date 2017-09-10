package com.github.jactorrises.model.internal.domain.guestbook;

import com.github.jactorrises.model.internal.JactorModule;
import com.github.jactorrises.model.internal.persistence.entity.guestbook.GuestBookEntity;
import com.github.jactorrises.model.internal.persistence.entity.user.UserEntity;
import com.github.jactorrises.model.internal.persistence.repository.HibernateRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static com.github.jactorrises.model.internal.domain.address.AddressDomain.anAddress;
import static com.github.jactorrises.model.internal.domain.guestbook.GuestBookDomain.aGuestBook;
import static com.github.jactorrises.model.internal.domain.person.PersonDomain.aPerson;
import static com.github.jactorrises.model.internal.domain.user.UserDomain.aUser;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = JactorModule.class)
@Transactional
public class GuestBookIntegrationTest {

    @Resource(name = "jactor-session") private SessionFactory sessionFactory;

    @Resource private HibernateRepository hibernateRepository;

    @Test public void willSaveGuestBookEntityToThePersistentLayer() {
        final UserEntity aPersistedUser = aPersistedUser();
        Long id = hibernateRepository.saveOrUpdate(aGuestBook().withTitle("my guest book").with(aPersistedUser).build().getEntity()).getId();

        session().flush();
        session().clear();

        GuestBookEntity guestBook = hibernateRepository.load(GuestBookEntity.class, id);

        assertThat(guestBook.getTitle()).isEqualTo("my guest book");
        assertThat(guestBook.getUser().getId()).isEqualTo(aPersistedUser.getId());
    }

    private UserEntity aPersistedUser() {
        UserEntity userEntity = aUser().withUserName("titten")
                .withPassword("demo")
                .withEmailAddress("jactor@rises")
                .with(aPerson().withDescription("description")
                        .with(anAddress().withAddressLine1("the streets")
                                .withCity("Dirdal")
                                .withCountry("NO")
                                .withZipCode(1234)
                        )
                )
                .build().getEntity();

        hibernateRepository.save(userEntity);

        return userEntity;
    }

    private Session session() {
        return sessionFactory.getCurrentSession();
    }
}
