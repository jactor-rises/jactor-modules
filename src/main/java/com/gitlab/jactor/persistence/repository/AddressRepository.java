package com.gitlab.jactor.persistence.repository;

import com.gitlab.jactor.persistence.entity.address.AddressEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AddressRepository extends CrudRepository<AddressEntity, Long> {
    List<AddressEntity> findByZipCode(Integer zipCode);
}
