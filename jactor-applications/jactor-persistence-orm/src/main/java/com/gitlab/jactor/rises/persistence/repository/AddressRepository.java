package com.gitlab.jactor.rises.persistence.repository;

import com.gitlab.jactor.rises.persistence.entity.address.AddressEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AddressRepository extends CrudRepository<AddressEntity, Long> {
    List<AddressEntity> findByZipCode(Integer zipCode);
}
