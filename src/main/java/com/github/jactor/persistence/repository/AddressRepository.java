package com.github.jactor.persistence.repository;

import com.github.jactor.persistence.entity.AddressEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AddressRepository extends CrudRepository<AddressEntity, Long> {
    List<AddressEntity> findByZipCode(String zipCode);
}
