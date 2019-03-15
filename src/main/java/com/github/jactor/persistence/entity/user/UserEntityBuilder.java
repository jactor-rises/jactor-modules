package com.github.jactor.persistence.entity.user;

import com.github.jactor.persistence.builder.AbstractBuilder;
import com.github.jactor.persistence.builder.MissingFields;
import com.github.jactor.persistence.entity.person.PersonEntity;
import com.github.jactor.persistence.entity.user.UserEntity.UserType;
import java.util.Optional;

public class UserEntityBuilder extends AbstractBuilder<UserEntity> {

  private String emailAddress;
  private PersonEntity person;
  private String username;

  UserEntityBuilder() {
    super(UserEntityBuilder::validate);
  }

  public UserEntityBuilder with(PersonEntity person) {
    this.person = person;
    return this;
  }

  public UserEntityBuilder withEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
    return this;
  }

  public UserEntityBuilder withUsername(String username) {
    this.username = username;
    return this;
  }

  @Override
  public UserEntity buildBean() {
    UserEntity useruserEntity = new UserEntity();
    useruserEntity.setEmailAddress(emailAddress);
    useruserEntity.setPersonEntity(person);
    useruserEntity.setUsername(username);
    useruserEntity.setUserType(UserType.ACTIVE);

    return useruserEntity;
  }

  private static Optional<MissingFields> validate(UserEntity userEntity, MissingFields missingFields) {
    missingFields.addInvalidFieldWhenNoValue(UserEntity.class.getSimpleName(), "username", userEntity.getUsername());
    missingFields.addInvalidFieldWhenNoValue(UserEntity.class.getSimpleName(), "personEntity", userEntity.getPerson());

    return missingFields.presentWhenFieldsAreMissing();
  }
}
