package com.github.jactorrises.model.internal.domain.guestbook;

import com.github.jactorrises.model.internal.JactorModule;
import com.github.jactorrises.model.internal.persistence.entity.guestbook.GuestBookEntity;
import com.github.jactorrises.model.internal.persistence.entity.user.UserEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;

import static com.github.jactorrises.model.internal.domain.address.AddressDomain.anAddress;
import static com.github.jactorrises.model.internal.domain.guestbook.GuestBookDomain.aGuestBook;
import static com.github.jactorrises.model.internal.domain.person.PersonDomain.aPerson;
import static com.github.jactorrises.model.internal.domain.user.UserDomain.aUser;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = JactorModule.class)
@Transactional
public class GuestBookIntegrationTest {

    @Resource(name = "jactor-session")
    private SessionFactory sessionFactory;

    @Test public void willSaveGuestBookEntityToThePersistentLayer() {
        final UserEntity aPersistedUser = aPersistedUser();
        Serializable id = session().save(aGuestBook().withTitleAs("my guest book").with(aPersistedUser).build().getEntity());

        session().flush();
        session().clear();

        GuestBookEntity guestBook = session().get(GuestBookEntity.class, id);

        assertThat(guestBook.getTitle()).isEqualTo("my guest book");
        assertThat(guestBook.getUser().getId()).isEqualTo(aPersistedUser.getId());
    }

    private UserEntity aPersistedUser() {
        UserEntity userEntity = aUser().withUserNameAs("titten")
                .withPasswordAs("demo")
                .withEmailAddressAs("jactor@rises")
                .with(aPerson().withDescriptionAs("description")
                        .with(anAddress().withAddressLine1As("the streets")
                                .withCityAs("Dirdal")
                                .withCountryAs("NO")
                                .withZipCodeAs(1234)
                        )
                )
                .build().getEntity();

        session().save(userEntity);

        return userEntity;
    }

    private Session session() {
        return sessionFactory.getCurrentSession();
    }
}
