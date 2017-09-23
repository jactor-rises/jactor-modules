package com.github.jactorrises.model.domain.blog;

import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.model.JactorModule;
import com.github.jactorrises.model.persistence.entity.blog.BlogEntity;
import com.github.jactorrises.model.persistence.entity.blog.BlogEntryEntity;
import com.github.jactorrises.model.persistence.entity.user.UserEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

import static com.github.jactorrises.model.domain.address.AddressDomain.anAddress;
import static com.github.jactorrises.model.domain.blog.BlogDomain.aBlog;
import static com.github.jactorrises.model.domain.blog.BlogEntryDomain.aBlogEntry;
import static com.github.jactorrises.model.domain.person.PersonDomain.aPerson;
import static com.github.jactorrises.model.domain.user.UserDomain.aUser;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = JactorModule.class)
@Transactional
public class BlogEntryIntegrationTest {

    @Autowired
    private SessionFactory sessionFactory;

    @Test public void willSaveBlogEntryEntityToThePersistentLayer() {
        Serializable id = session().save(aBlogEntry().with(aPersistedBlogTitled("my blog")).withEntry("svada", "lada").build().getEntity());

        session().flush();
        session().clear();

        BlogEntryEntity blogEntry = session().get(BlogEntryEntity.class, id);

        assertThat(blogEntry.getBlog().getTitle()).as("blog.title").isEqualTo("my blog");
        assertThat(blogEntry.getCreatedTime()).as("entry.createdTime").isNotNull();
        assertThat(blogEntry.getCreatorName()).as("entry.creator").isEqualTo(new Name("lada"));
        assertThat(blogEntry.getEntry()).as("entry.entry").isEqualTo("svada");
    }

    private BlogEntity aPersistedBlogTitled(@SuppressWarnings("SameParameterValue") String blogTitled) {
        BlogEntity blogEntity = aBlog().with(aPersistedUser()).withTitleAs(blogTitled).build().getEntity();
        session().save(blogEntity);

        return blogEntity;
    }

    private UserEntity aPersistedUser() {
        UserEntity userEntity = aUser().withUserName("titten")
                .withEmailAddress("jactor@rises")
                .with(aPerson().withDescription("description")
                        .with(anAddress().withAddressLine1("the streets")
                                .withCity("Dirdal")
                                .withCountry("NO")
                                .withZipCode(1234)
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
