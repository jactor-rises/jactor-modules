package com.github.jactorrises.facade.config.db;

import nu.hjemme.client.datatype.Country;
import nu.hjemme.client.datatype.Description;
import nu.hjemme.client.datatype.EmailAddress;
import nu.hjemme.client.domain.Address;
import com.github.jactorrises.facade.config.JactorDbContext;
import nu.hjemme.persistence.client.UserEntity;
import org.assertj.core.api.SoftAssertions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static nu.hjemme.business.domain.AddressDomain.anAddress;
import static nu.hjemme.business.domain.PersonDomain.aPerson;
import static nu.hjemme.business.domain.UserDomain.aUser;
import static org.hibernate.criterion.Restrictions.eq;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = JactorDbContext.class)
@Transactional
public class UserDbIntegrationTest {

    @Resource(name = "sessionFactory")
    private SessionFactory sessionFactory;

    @Test
    public void willSaveUserAndPersonToTheDatabase() {
        session().save(
                aUser().withUserNameAs("titten")
                        .withPasswordAs("demo")
                        .withEmailAddressAs("helt@hjemme")
                        .with(aPerson().withDescriptionAs("description")
                                .with(anAddress().withAddressLine1As("Hjemme")
                                        .withCityAs("Dirdal")
                                        .withCountryAs("NO")
                                        .withZipCodeAs(1234)
                                )
                        ).build().getEntity()
        );

        session().flush();
        session().clear();

        UserEntity userFromDb = (UserEntity) session().createCriteria(UserEntity.class).add(eq("userName", "titten")).uniqueResult();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(userFromDb.getEmailAddress()).as("user.emailAddress").isEqualTo(new EmailAddress("helt", "hjemme"));
            softly.assertThat(userFromDb.getPassword()).as("user.password").isEqualTo("demo");
            softly.assertThat(userFromDb.getPerson().getDescription()).as("user.description").isEqualTo(new Description("description"));
        });
    }

    @Test
    public void willSaveUserWithAddressTheDatabase() {
        session().save(
                aUser().withUserNameAs("titten")
                        .withPasswordAs("demo")
                        .with(aPerson().withDescriptionAs("description")
                                .with(anAddress().withAddressLine1As("Hjemme")
                                        .withCityAs("Dirdal")
                                        .withCountryAs("NO")
                                        .withZipCodeAs(1234)
                                )
                        ).build().getEntity()
        );

        session().flush();
        session().clear();

        UserEntity userFromDb = (UserEntity) session().createCriteria(UserEntity.class).add(eq("userName", "titten")).uniqueResult();
        Address address = userFromDb.getPerson().getAddress();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(address.getAddressLine1()).as("address line 1").isEqualTo("Hjemme");
            softly.assertThat(address.getAddressLine2()).as("address line 2").isNull();
            softly.assertThat(address.getAddressLine3()).as("address line 3").isNull();
            softly.assertThat(address.getCity()).as("city").isEqualTo("Dirdal");
            softly.assertThat(address.getCountry()).as("country").isEqualTo(new Country("NO"));
            softly.assertThat(address.getZipCode()).as("zip code").isEqualTo(1234);
        });
    }

    private Session session() {
        return sessionFactory.getCurrentSession();
    }
}
