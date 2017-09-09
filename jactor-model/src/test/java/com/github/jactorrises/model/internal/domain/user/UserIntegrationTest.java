package com.github.jactorrises.model.internal.domain.user;

import com.github.jactorrises.client.datatype.Country;
import com.github.jactorrises.client.datatype.Description;
import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.client.domain.Address;
import com.github.jactorrises.model.internal.JactorModule;
import com.github.jactorrises.model.internal.persistence.client.dao.UserDao;
import com.github.jactorrises.model.internal.persistence.entity.user.UserEntity;
import org.assertj.core.api.SoftAssertions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.github.jactorrises.model.internal.domain.address.AddressDomain.anAddress;
import static com.github.jactorrises.model.internal.domain.person.PersonDomain.aPerson;
import static com.github.jactorrises.model.internal.domain.user.UserDomain.aUser;
import static org.hibernate.criterion.Restrictions.eq;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = JactorModule.class)
@Transactional
public class UserIntegrationTest {

    @Autowired private SessionFactory sessionFactory;

    @Autowired private UserDao userDao;

    @Test
    public void willSaveUserAndPersonToTheDatabase() {
        userDao.save(
                aUser().withUserName("titten")
                        .withPassword("demo")
                        .withEmailAddress("jactorhrises")
                        .with(aPerson().withDescription("description")
                                .with(anAddress().withAddressLine1("the streets")
                                        .withCity("Dirdal")
                                        .withCountry("NO")
                                        .withZipCode(1234)
                                )
                        ).build().getEntity()
        );

        session().flush();
        session().clear();

        Optional<UserEntity> possibleUserFromDb = userDao.findUsing(new UserName("titten"));

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(possibleUserFromDb).isPresent();
            UserEntity userFromDb = possibleUserFromDb.get();
//            softly.assertThat(userFromDb.getEmailAddress()).as("user.emailAddress").isEqualTo(new EmailAddress("jactor", "rises")); todo fix: should work after story   ..#131 is resolved
            softly.assertThat(userFromDb.getPassword()).as("user.password").isEqualTo("demo");
            softly.assertThat(userFromDb.getPerson().getDescription()).as("user.description").isEqualTo(new Description("description"));
        });
    }

    @Test
    public void willSaveUserWithAddressTheDatabase() {
        userDao.save(
                aUser().withUserName("titten")
                        .withPassword("demo")
                        .with(aPerson().withDescription("description")
                                .with(anAddress().withAddressLine1("the streets")
                                        .withCity("Dirdal")
                                        .withCountry("NO")
                                        .withZipCode(1234)
                                )
                        ).build().getEntity()
        );

        session().flush();
        session().clear();

        Optional<UserEntity> possibleUserFromDb = userDao.findUsing(new UserName("titten"));

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(possibleUserFromDb).isPresent();
            UserEntity userFromDb = possibleUserFromDb.get();
            Address address = userFromDb.getPerson().getAddress();
            softly.assertThat(address.getAddressLine1()).as("address line 1").isEqualTo("the streets");
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
