package com.github.jactor.rises.persistence.repository;

import com.github.jactor.rises.persistence.entity.address.AddressEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AddressRepository extends CrudRepository<AddressEntity, Long> {
    List<AddressEntity> findByZipCode(Integer zipCode);
}
