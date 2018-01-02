package com.github.jactorrises.model.domain.user;

import com.github.jactorrises.client.datatype.EmailAddress;
import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.client.domain.User;
import com.github.jactorrises.client.persistence.dto.UserDto;
import com.github.jactorrises.model.domain.PersistentDomain;
import com.github.jactorrises.model.domain.person.PersonDomain;

public class UserDomain extends PersistentDomain implements User {

    private final UserDto userDto;

    public UserDomain(UserDto userDto) {
        this.userDto = userDto;
    }

    @Override public UserName getUserName() {
        return userDto.getUserName() != null ? new UserName(userDto.getUserName()) : null;
    }

    @Override public PersonDomain getPerson() {
        return userDto.getPerson() != null ? new PersonDomain(userDto.getPerson()) : null;
    }

    @Override public EmailAddress getEmailAddress() {
        return new EmailAddress(userDto.getEmailAddress());
    }

    @Override public UserDto getDto() {
        return userDto;
    }

    public static UserBuilder aUser() {
        return new UserBuilder();
    }
}
