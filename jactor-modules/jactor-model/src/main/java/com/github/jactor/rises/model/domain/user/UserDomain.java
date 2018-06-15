package com.github.jactor.rises.model.domain.user;

import com.github.jactor.rises.client.datatype.EmailAddress;
import com.github.jactor.rises.client.datatype.Username;
import com.github.jactor.rises.client.domain.User;
import com.github.jactor.rises.client.dto.UserDto;
import com.github.jactor.rises.model.domain.PersistentDomain;
import com.github.jactor.rises.model.domain.person.PersonDomain;

import java.util.Optional;

public class UserDomain extends PersistentDomain implements User {

    private final UserDto userDto;

    public UserDomain(UserDto userDto) {
        this.userDto = userDto;
    }

    @Override public Username getUsername() {
        return Optional.ofNullable(userDto.getUsername()).map(Username::new).orElse(null);
    }

    @Override public PersonDomain getPerson() {
        return Optional.ofNullable(userDto.getPerson()).map(PersonDomain::new).orElse(null);
    }

    @Override public EmailAddress getEmailAddress() {
        return Optional.ofNullable(userDto.getEmailAddress()).map(EmailAddress::new).orElse(null);
    }

    @Override public UserDto getDto() {
        return userDto;
    }

    public static UserBuilder aUser() {
        return new UserBuilder();
    }
}
