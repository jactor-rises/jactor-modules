package com.github.jactor.rises.model.domain.user;

import com.github.jactorrises.client.datatype.EmailAddress;
import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.client.domain.GuestBook;
import com.github.jactorrises.client.domain.User;
import com.github.jactorrises.client.dto.UserDto;
import com.github.jactor.rises.model.domain.PersistentDomain;
import com.github.jactor.rises.model.domain.blog.BlogDomain;
import com.github.jactor.rises.model.domain.guestbook.GuestBookDomain;
import com.github.jactor.rises.model.domain.person.PersonDomain;

public class UserDomain extends PersistentDomain implements User {

    private final UserDto userDto;

    public UserDomain(UserDto userDto) {
        this.userDto = userDto;
    }

    @Override public UserName getUserName() {
        return userDto.getUserName() != null ? new UserName(userDto.getUserName()) : null;
    }

    @Override public BlogDomain getBlog() {
        return userDto.getBlog() != null ? new BlogDomain(userDto.getBlog()) : null;
    }

    @Override public GuestBook getGuestBook() {
        return userDto.getGuestBook() != null ? new GuestBookDomain(userDto.getGuestBook()) : null;
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
