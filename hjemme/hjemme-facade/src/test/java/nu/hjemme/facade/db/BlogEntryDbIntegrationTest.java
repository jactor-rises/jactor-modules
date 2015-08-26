package nu.hjemme.facade.db;

import nu.hjemme.client.datatype.Name;
import nu.hjemme.facade.config.HjemmeBeanContext;
import nu.hjemme.facade.service.MenuFacadeIntegrationTest;
import nu.hjemme.persistence.BlogEntity;
import nu.hjemme.persistence.BlogEntryEntity;
import nu.hjemme.persistence.UserEntity;
import nu.hjemme.persistence.config.HjemmeDbContext;
import nu.hjemme.persistence.db.DefaultBlogEntryEntity;
import nu.hjemme.test.matcher.MatchBuilder;
import nu.hjemme.test.matcher.TypeSafeBuildMatcher;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.Serializable;

import static nu.hjemme.business.domain.builder.DomainBuilder.aBlog;
import static nu.hjemme.business.domain.builder.DomainBuilder.aBlogEntry;
import static nu.hjemme.business.domain.builder.DomainBuilder.aPerson;
import static nu.hjemme.business.domain.builder.DomainBuilder.aUser;
import static nu.hjemme.business.domain.builder.DomainBuilder.anAddress;
import static nu.hjemme.test.matcher.DescriptionMatcher.is;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {HjemmeBeanContext.class, MenuFacadeIntegrationTest.HjemmeTestMenus.class, HjemmeDbContext.class})
@Transactional
public class BlogEntryDbIntegrationTest {

    @Resource(name = "sessionFactory") @SuppressWarnings("unused") // initialized by spring
    private SessionFactory sessionFactory;
    private JdbcTemplate jdbcTemplate;

    @Resource(name = "dataSource") @SuppressWarnings("unused") // used by spring
    private void initJdbcTemple(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Test public void willSaveBlogEntryEntityToThePersistentLayer() {
        Serializable id = session().save(aBlogEntry().with(aPersistedBlogTitled("my blog")).withEntryAs("svada", "lada").get().getEntity());

        session().flush();
        session().clear();

        assertThat((BlogEntryEntity) session().get(DefaultBlogEntryEntity.class, id), new TypeSafeBuildMatcher<BlogEntryEntity>("blog entry persisted") {
            @Override public MatchBuilder matches(BlogEntryEntity typeToTest, MatchBuilder matchBuilder) {
                return matchBuilder
                        .matches(typeToTest.getBlog().getTitle(), is(equalTo("my blog"), "blog.title"))
                        .matches(typeToTest.getEntry().getCreatedTime(), is(notNullValue(), "entry.createdTime"))
                        .matches(typeToTest.getEntry().getCreatorName(), is(equalTo(new Name("lada")), "entry.creator"))
                        .matches(typeToTest.getEntry().getEntry(), is(equalTo("svada"), "entry.entry"));
            }
        });
    }

    private BlogEntity aPersistedBlogTitled(String blogTitled) {
        BlogEntity blogEntity = aBlog().with(aPersistedUser()).withTitleAs(blogTitled).get().getEntity();
        session().save(blogEntity);
        return blogEntity;
    }

    private UserEntity aPersistedUser() {
        UserEntity userEntity = aUser().withUserNameAs("titten")
                .withPasswordAs("demo")
                .withEmailAddressAs("helt@hjemme")
                .with(aPerson().withDescriptionAs("description")
                                .with(anAddress().withAddressLine1As("Hjemme")
                                                .withCityAs("Dirdal")
                                                .withCountryAs("NO", "no")
                                                .withZipCodeAs(1234)
                                )
                )
                .get().getEntity();

        session().save(userEntity);

        return userEntity;
    }

    private Session session() {
        return sessionFactory.getCurrentSession();
    }
}
