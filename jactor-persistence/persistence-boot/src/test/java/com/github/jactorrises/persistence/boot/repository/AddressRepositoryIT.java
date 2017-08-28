package com.github.jactorrises.persistence.boot.repository;

import com.github.jactorrises.persistence.boot.Persistence;
import com.github.jactorrises.persistence.boot.entity.address.AddressEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static com.github.jactorrises.persistence.boot.entity.address.AddressEntity.anAddress;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Persistence.class)
@Transactional
public class AddressRepositoryIT {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private SessionFactory sessionFactory;

    @Test
    public void shouldPFindPersistedEntity() {
        assertThat(session().createCriteria(AddressEntity.class).list()).as("before").isEmpty();

        Long id = addressRepository.saveOrUpdate(
                anAddress()
                        .withAddressLine1("living on the edge")
                        .withZipCode(1234)
                        .withCity("metropolis")
                        .build()
        ).getId();

        assertSoftly(softly -> {
            softly.assertThat(id).as("id").isNotNull();
            softly.assertThat(session().createCriteria(AddressEntity.class).list()).as("after").isNotEmpty();
        });
    }

    @Test
    public void shouldReadPersistedEntityWithHibernate() {
        assertThat(session().createCriteria(AddressEntity.class).list()).as("before").isEmpty();

        Long id = addressRepository.saveOrUpdate(
                anAddress()
                        .withAddressLine1("living on the edge")
                        .withZipCode(1234)
                        .withCity("metropolis")
                        .build()
        ).getId();

        AddressEntity addressEntity = session().load(AddressEntity.class, id);

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
