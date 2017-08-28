package com.github.jactorrises.persistence.boot.repository;

import com.github.jactorrises.persistence.boot.entity.address.AddressEntity;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<AddressEntity, Long> {

}
