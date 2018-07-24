package com.gitlab.jactor.rises.model.domain.user;

import com.gitlab.jactor.rises.commons.datatype.EmailAddress;
import com.gitlab.jactor.rises.commons.datatype.Username;
import com.gitlab.jactor.rises.model.domain.User;
import com.gitlab.jactor.rises.commons.dto.UserDto;
import com.gitlab.jactor.rises.model.domain.PersistentDomain;
import com.gitlab.jactor.rises.model.domain.person.PersonDomain;

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
