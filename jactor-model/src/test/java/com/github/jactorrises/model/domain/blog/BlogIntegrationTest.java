package com.github.jactorrises.model.domain.blog;

import com.github.jactorrises.model.JactorModel;
import com.github.jactorrises.persistence.client.dto.UserDto;
import com.github.jactorrises.persistence.entity.blog.BlogOrm;
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

import static com.github.jactorrises.model.domain.blog.BlogDomain.aBlog;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = JactorModel.class)
@Transactional
@Ignore("#176: rewrite using rest from persistence-orm")
public class BlogIntegrationTest {

    @SuppressWarnings("SpringJavaAutowiringInspection") // located in jactor-persistence-orm...
    @Resource private SessionFactory sessionFactory;

    @Test public void willSaveBlogOrmToThePersistentLayer() {
        final UserDto persistedUser = persistUser();
        Serializable id = session().save(aBlog().withTitleAs("some blog").with(persistedUser).build().getDto());

        session().flush();
        session().clear();

        BlogOrm blog = session().get(BlogOrm.class, id);

        assertThat(blog.getCreated()).isEqualTo(LocalDate.now());
        assertThat(blog.getTitle()).isEqualTo("some blog");
        assertThat(blog.getUser().getId()).isEqualTo(persistedUser.getId());
    }

    private UserDto persistUser() {
//        UserOrm userEntity = (UserOrm) aUser().withUserName("titten")
//                .withEmailAddress("jactor@rises")
//                .with(aPerson()
//                        .withSurname("nevland")
//                        .withDescription("description")
//                        .with(anAddress().withAddressLine1("the streets")
//                                .withCity("Dirdal")
//                                .withCountry("NO")
//                                .withZipCode(1234)
//                        )
//                ).build()
//                .getDto();
//
//        session().save(userEntity);

//        return userEntity;
        return new UserDto();
    }

    private Session session() {
        return sessionFactory.getCurrentSession();
    }
}
