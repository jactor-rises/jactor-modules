package com.github.jactor.web.dto;

import com.github.jactor.shared.dto.UserDto;
import java.util.Optional;

public class UserDtoBuilder {

  private PersonDtoBuilder personDtoBuilder;
  private String username;

  public UserDtoBuilder with(PersonDtoBuilder personDtoBuilder) {
    this.personDtoBuilder = personDtoBuilder;
    return this;
  }

  public UserDtoBuilder withUsername(String username) {
    this.username = username;
    return this;
  }

  public UserDto build() {
    UserDto userDto = new UserDto();
    userDto.setPerson(Optional.ofNullable(personDtoBuilder).map(PersonDtoBuilder::build).orElse(null));
    userDto.setUsername(username);

    return userDto;
  }
}
