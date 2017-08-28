package com.github.jactorrises.persistence.boot.repository;

import com.github.jactorrises.persistence.boot.entity.address.AddressEntity;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AddressRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public AddressRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    public AddressEntity saveOrUpdate(AddressEntity addressEntity) {
        sessionFactory.getCurrentSession().saveOrUpdate(addressEntity);
        return addressEntity;
    }
}
