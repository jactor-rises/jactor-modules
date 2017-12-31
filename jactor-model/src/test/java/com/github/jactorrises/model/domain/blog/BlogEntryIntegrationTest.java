package com.github.jactorrises.model.domain.blog;

import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.model.JactorModel;
import com.github.jactorrises.persistence.client.dto.BlogDto;
import com.github.jactorrises.persistence.client.dto.UserDto;
import com.github.jactorrises.persistence.entity.blog.BlogEntity;
import com.github.jactorrises.persistence.entity.blog.BlogEntryEntity;
import com.github.jactorrises.persistence.entity.user.UserEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Ignore;
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
@Ignore("#176: rewrite using rest from persistence-orm")
public class BlogEntryIntegrationTest {

    @SuppressWarnings({"SpringJavaInjectionPointsAutowiringInspection"}) // located in jactor-persistence-orm...
    @Autowired private SessionFactory sessionFactory;

    @Test public void willSaveBlogEntryEntityToThePersistentLayer() {
        Serializable id = session().save(aBlogEntry().with(aPersistedBlogTitled("my blog")).withEntry("some").withCreatorName("thing").build().getDto());

        session().flush();
        session().clear();

        BlogEntryEntity blogEntry = session().get(BlogEntryEntity.class, id);

        assertThat(blogEntry.getBlog().getTitle()).as("blog.title").isEqualTo("my blog");
        assertThat(blogEntry.getCreatedTime()).as("entry.createdTime").isNotNull();
        assertThat(blogEntry.getCreatorName()).as("entry.creator").isEqualTo(new Name("thing"));
        assertThat(blogEntry.getEntry()).as("entry.entry").isEqualTo("some");
    }

    private BlogDto aPersistedBlogTitled(@SuppressWarnings("SameParameterValue") String blogTitled) {
        BlogDto blogDto = aBlog().with(aPersistedUser()).withTitleAs(blogTitled).build().getDto();
        session().save(new BlogEntity(blogDto));

        return blogDto;
    }

    private UserDto aPersistedUser() {
        UserDto userDto = aUser().withUserName("titten")
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
                .build().getDto();

        session().save(new UserEntity(userDto));

        return userDto;
    }

    private Session session() {
        return sessionFactory.getCurrentSession();
    }
}
