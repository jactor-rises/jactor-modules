package com.github.jactor.web.dto;

import com.github.jactor.shared.dto.PersonDto;
import java.util.Optional;

public class PersonDtoBuilder {

  private AddressDtoBuilder addressDtoBuilder;
  private String description;
  private String firstName;
  private String surname;

  public PersonDtoBuilder withFirstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  public PersonDtoBuilder withSurname(String surname) {
    this.surname = surname;
    return this;
  }

  public PersonDtoBuilder withDescription(String description) {
    this.description = description;
    return this;
  }

  PersonDto build() {
    PersonDto personDto = new PersonDto();
    personDto.setAddress(Optional.ofNullable(addressDtoBuilder).map(AddressDtoBuilder::build).orElse(null));
    personDto.setDescription(description);
    personDto.setFirstName(firstName);
    personDto.setSurname(surname);

    return personDto;
  }

  public PersonDtoBuilder with(AddressDtoBuilder addressDtoBuilder) {
    this.addressDtoBuilder = addressDtoBuilder;
    return this;
  }
}
