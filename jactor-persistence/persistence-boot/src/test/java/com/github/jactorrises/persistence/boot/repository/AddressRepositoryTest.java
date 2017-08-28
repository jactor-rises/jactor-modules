package com.github.jactorrises.persistence.boot.repository;

import com.github.jactorrises.persistence.boot.Persistence;
import com.github.jactorrises.persistence.boot.entity.address.AddressEntity;
import org.hibernate.SessionFactory;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static com.github.jactorrises.persistence.boot.entity.address.AddressEntity.anAddress;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Persistence.class)
@Transactional
@Ignore
public class AddressRepositoryTest {

    @Autowired
    private com.github.jactorrises.persistence.boot.repository.AddressRepository addressRepository;

    @Autowired
    private SessionFactory sessionFactory;

    @Test
    public void shouldPFindPersistedEntity() {
        assertThat(addressRepository.findAll().iterator()).isEmpty();

        Long id = addressRepository.save(
                anAddress()
                        .withAddressLine1("living on the edge")
                        .withZipCode(1234)
                        .withCity("metropolis")
                        .build()
        ).getId();

        assertThat(addressRepository.findAll().iterator()).isNotEmpty();
        assertThat(id).isNotNull();
    }

    @Test
    public void shouldReadPersistedEntityWithHibernate() {
        assertThat(addressRepository.findAll().iterator()).isEmpty();

        Long id = addressRepository.save(
                anAddress()
                        .withAddressLine1("living on the edge")
                        .withZipCode(1234)
                        .withCity("metropolis")
                        .build()
        ).getId();

        sessionFactory.getCurrentSession().load(AddressEntity.class, id);
        fail("assertions");
    }
}
