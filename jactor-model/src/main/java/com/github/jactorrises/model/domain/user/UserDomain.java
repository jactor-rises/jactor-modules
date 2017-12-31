package com.github.jactorrises.model.domain.user;

import com.github.jactorrises.client.datatype.EmailAddress;
import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.client.domain.User;
import com.github.jactorrises.model.domain.PersistentDomain;
import com.github.jactorrises.model.domain.person.PersonDomain;
import com.github.jactorrises.persistence.client.dto.UserDto;

public class UserDomain extends PersistentDomain<Long> implements User {

    private final UserDto userDto;

    public UserDomain(UserDto userDto) {
        this.userDto = userDto;
    }

    @Override public UserName getUserName() {
        return userDto.getUserName();
    }

    @Override public PersonDomain getPerson() {
        return userDto.getPerson() != null ? new PersonDomain(userDto.getPerson()) : null;
    }

    @Override public EmailAddress getEmailAddress() {
        return userDto.getEmailAddress();
    }

    @Override public boolean isUserNameEmailAddress() {
        return userDto.isUserNameEmailAddress();
    }

    @Override public UserDto getPersistence() {
        return userDto;
    }

    public static UserBuilder aUser() {
        return new UserBuilder();
    }
}
