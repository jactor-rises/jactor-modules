package com.github.jactorrises.facade.config.db;

import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.facade.JactorModule;
import com.github.jactorrises.facade.config.JactorDbContext;
import com.github.jactorrises.persistence.client.BlogEntity;
import com.github.jactorrises.persistence.client.BlogEntryEntity;
import com.github.jactorrises.persistence.client.UserEntity;
import com.github.jactorrises.persistence.orm.domain.DefaultBlogEntryEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;

import static com.github.jactorrises.business.domain.AddressDomain.anAddress;
import static com.github.jactorrises.business.domain.BlogDomain.aBlog;
import static com.github.jactorrises.business.domain.BlogEntryDomain.aBlogEntry;
import static com.github.jactorrises.business.domain.PersonDomain.aPerson;
import static com.github.jactorrises.business.domain.UserDomain.aUser;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JactorModule.class, JactorDbContext.class})
@Transactional
public class BlogEntryDbIntegrationTest {

    @Resource(name = "sessionFactory") @SuppressWarnings("unused") // initialized by spring
    private SessionFactory sessionFactory;

    @Test public void willSaveBlogEntryEntityToThePersistentLayer() {
        Serializable id = session().save(aBlogEntry().with(aPersistedBlogTitled("my blog")).withEntryAs("svada", "lada").build().getEntity());

        session().flush();
        session().clear();

        BlogEntryEntity blogEntry = (BlogEntryEntity) session().get(DefaultBlogEntryEntity.class, id);

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
