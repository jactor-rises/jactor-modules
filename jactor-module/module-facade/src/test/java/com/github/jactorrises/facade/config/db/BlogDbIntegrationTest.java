package com.github.jactorrises.facade.config.db;

import com.github.jactorrises.facade.config.JactorBeanContext;
import com.github.jactorrises.facade.config.JactorDbContext;
import nu.hjemme.persistence.client.BlogEntity;
import nu.hjemme.persistence.client.UserEntity;
import nu.hjemme.persistence.orm.domain.DefaultBlogEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.time.LocalDate;

import static nu.hjemme.business.domain.AddressDomain.anAddress;
import static nu.hjemme.business.domain.BlogDomain.aBlog;
import static nu.hjemme.business.domain.PersonDomain.aPerson;
import static nu.hjemme.business.domain.UserDomain.aUser;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JactorBeanContext.class, JactorDbContext.class})
@Transactional
public class BlogDbIntegrationTest {

    @Resource(name = "sessionFactory")
    private SessionFactory sessionFactory;

    @Test public void willSaveBlogEntityToThePersistentLayer() {
        final UserEntity persistedUser = persistUser();
        Serializable id = session().save(aBlog().withTitleAs("some blog").with(persistedUser).build().getEntity());

        session().flush();
        session().clear();

        BlogEntity blog = (BlogEntity) session().get(DefaultBlogEntity.class, id);

        assertThat(blog.getCreated()).isEqualTo(LocalDate.now());
        assertThat(blog.getTitle()).isEqualTo("some blog");
        assertThat(blog.getUser().getId()).isEqualTo(persistedUser.getId());
    }

    private UserEntity persistUser() {
        UserEntity userEntity = aUser().withUserNameAs("titten")
                .withPasswordAs("demo")
                .withEmailAddressAs("helt@hjemme")
                .with(aPerson().withDescriptionAs("description")
                        .with(anAddress().withAddressLine1As("Hjemme")
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
