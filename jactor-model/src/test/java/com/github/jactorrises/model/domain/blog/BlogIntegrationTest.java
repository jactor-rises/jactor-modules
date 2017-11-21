package com.github.jactorrises.model.domain.blog;

import com.github.jactorrises.model.JactorModel;
import com.github.jactorrises.persistence.entity.blog.BlogOrm;
import com.github.jactorrises.persistence.entity.user.UserOrm;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.time.LocalDate;

import static com.github.jactorrises.model.domain.address.AddressDomain.anAddress;
import static com.github.jactorrises.model.domain.blog.BlogDomain.aBlog;
import static com.github.jactorrises.model.domain.person.PersonDomain.aPerson;
import static com.github.jactorrises.model.domain.user.UserDomain.aUser;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = JactorModel.class)
@Transactional
@Ignore(value = "#168: fix service")
public class BlogIntegrationTest {

    @Resource
    private SessionFactory sessionFactory;

    @Test public void willSaveBlogOrmToThePersistentLayer() {
        final UserOrm persistedUser = persistUser();
        Serializable id = session().save(aBlog().withTitleAs("some blog").with(persistedUser).build().getEntity());

        session().flush();
        session().clear();

        BlogOrm blog = session().get(BlogOrm.class, id);

        assertThat(blog.getCreated()).isEqualTo(LocalDate.now());
        assertThat(blog.getTitle()).isEqualTo("some blog");
        assertThat(blog.getUser().getId()).isEqualTo(persistedUser.getId());
    }

    private UserOrm persistUser() {
        UserOrm userEntity = (UserOrm) aUser().withUserName("titten")
                .withEmailAddress("jactor@rises")
                .with(aPerson()
                        .withSurname("nevland")
                        .withDescription("description")
                        .with(anAddress().withAddressLine1("the streets")
                                .withCity("Dirdal")
                                .withCountry("NO")
                                .withZipCode(1234)
                        )
                ).build()
                .getEntity();

        session().save(userEntity);

        return userEntity;
    }

    private Session session() {
        return sessionFactory.getCurrentSession();
    }
}
