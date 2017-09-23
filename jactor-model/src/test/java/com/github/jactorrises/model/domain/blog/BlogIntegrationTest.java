package com.github.jactorrises.model.domain.blog;

import com.github.jactorrises.model.JactorModule;
import com.github.jactorrises.model.persistence.entity.blog.BlogEntity;
import com.github.jactorrises.model.persistence.entity.user.UserEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
@ContextConfiguration(classes = JactorModule.class)
@Transactional
public class BlogIntegrationTest {

    @Resource
    private SessionFactory sessionFactory;

    @Test public void willSaveBlogEntityToThePersistentLayer() {
        final UserEntity persistedUser = persistUser();
        Serializable id = session().save(aBlog().withTitleAs("some blog").with(persistedUser).build().getEntity());

        session().flush();
        session().clear();

        BlogEntity blog = session().get(BlogEntity.class, id);

        assertThat(blog.getCreated()).isEqualTo(LocalDate.now());
        assertThat(blog.getTitle()).isEqualTo("some blog");
        assertThat(blog.getUser().getId()).isEqualTo(persistedUser.getId());
    }

    private UserEntity persistUser() {
        UserEntity userEntity = aUser().withUserName("titten")
                .withEmailAddress("jactor@rises")
                .with(aPerson().withDescription("description")
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
