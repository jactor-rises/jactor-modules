package com.github.jactorrises.persistence.boot.repository;

import com.github.jactorrises.persistence.boot.Persistence;
import com.github.jactorrises.persistence.boot.entity.address.AddressEntityImpl;
import com.github.jactorrises.persistence.client.AddressEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static com.github.jactorrises.persistence.boot.entity.address.AddressEntityImpl.anAddress;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Persistence.class)
@Transactional
public class HibernateRepositoryIT {

    @Autowired
    private HibernateRepository hibernateRepository;

    @Autowired
    private SessionFactory sessionFactory;

    @Test
    public void shouldFindAddress() {
        int noOfEntities = session().createCriteria(AddressEntity.class).list().size();

        Long id = hibernateRepository.saveOrUpdate(
                anAddress()
                        .withAddressLine1("living on the edge")
                        .withZipCode(1234)
                        .withCity("metropolis")
                        .build()
        ).getId();

        assertSoftly(softly -> {
            softly.assertThat(id).as("id").isNotNull();
            softly.assertThat(session().createCriteria(AddressEntity.class).list()).as("persisted entities").hasSize(noOfEntities + 1);
        });
    }

    @Test
    public void shouldReadAddressEntityWithHibernate() {
        Long id = hibernateRepository.saveOrUpdate(
                anAddress()
                        .withAddressLine1("living on the edge")
                        .withZipCode(1234)
                        .withCity("metropolis")
                        .build()
        ).getId();

        session().flush();
        session().clear();

        AddressEntity addressEntity = hibernateRepository.load(AddressEntityImpl.class, id);

        assertSoftly(softly -> {
            softly.assertThat(addressEntity.getAddressLine1()).as("addressLine1").isEqualTo("living on the edge");
            softly.assertThat(addressEntity.getZipCode()).as("zipCode").isEqualTo(1234);
            softly.assertThat(addressEntity.getCity()).as("city").isEqualTo("metropolis");
        });
    }

    private Session session() {
        return sessionFactory.getCurrentSession();
    }
}
