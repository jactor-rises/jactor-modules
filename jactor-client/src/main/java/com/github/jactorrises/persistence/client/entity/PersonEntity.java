package com.github.jactorrises.persistence.client.entity;

import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.client.domain.Person;

import java.util.Locale;

public interface PersonEntity extends Person {

    void setAddressEntity(AddressEntity adressEntity);

    void setDescription(String description);

    void setFirstName(Name firstName);

    void setSurname(Name surname);

    void setLocale(Locale locale);

    void setUserEntity(UserEntity userEntity);
}
