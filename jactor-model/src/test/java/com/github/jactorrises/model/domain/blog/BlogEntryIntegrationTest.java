package com.github.jactorrises.model.domain.blog;

import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.model.JactorModel;
import com.github.jactorrises.model.persistence.entity.blog.BlogEntryOrm;
import com.github.jactorrises.persistence.client.entity.BlogEntity;
import com.github.jactorrises.persistence.client.entity.UserEntity;
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
@ContextConfiguration(classes = JactorModel.class)
@Transactional
public class BlogEntryIntegrationTest {

    @Autowired
    private SessionFactory sessionFactory;

    @Test public void willSaveBlogEntryEntityToThePersistentLayer() {
        Serializable id = session().save(aBlogEntry().with(aPersistedBlogTitled("my blog")).withEntry("some").withCreatorName("thing").build().getEntity());

        session().flush();
        session().clear();

        BlogEntryOrm blogEntry = session().get(BlogEntryOrm.class, id);

        assertThat(blogEntry.getBlog().getTitle()).as("blog.title").isEqualTo("my blog");
        assertThat(blogEntry.getCreatedTime()).as("entry.createdTime").isNotNull();
        assertThat(blogEntry.getCreatorName()).as("entry.creator").isEqualTo(new Name("thing"));
        assertThat(blogEntry.getEntry()).as("entry.entry").isEqualTo("some");
    }

    private BlogEntity aPersistedBlogTitled(@SuppressWarnings("SameParameterValue") String blogTitled) {
        BlogEntity blogEntity = aBlog().with(aPersistedUser()).withTitleAs(blogTitled).build().getEntity();
        session().save(blogEntity);

        return blogEntity;
    }

    private UserEntity aPersistedUser() {
        UserEntity userEntity = aUser().withUserName("titten")
                .withEmailAddress("jactor@rises")
                .with(aPerson()
                        .withDescription("description")
                        .withSurname("jacobsen")
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
