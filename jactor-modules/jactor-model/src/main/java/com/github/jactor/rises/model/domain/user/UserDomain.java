package com.github.jactor.rises.model.domain.user;

import com.github.jactor.rises.client.datatype.EmailAddress;
import com.github.jactor.rises.client.datatype.UserName;
import com.github.jactor.rises.client.domain.GuestBook;
import com.github.jactor.rises.client.domain.User;
import com.github.jactor.rises.client.dto.UserDto;
import com.github.jactor.rises.model.domain.PersistentDomain;
import com.github.jactor.rises.model.domain.blog.BlogDomain;
import com.github.jactor.rises.model.domain.guestbook.GuestBookDomain;
import com.github.jactor.rises.model.domain.person.PersonDomain;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class UserDomain extends PersistentDomain implements User {

    private final UserDto userDto;

    public UserDomain(UserDto userDto) {
        this.userDto = userDto;
    }

    @Override public UserName getUserName() {
        return Optional.ofNullable(userDto.getUserName()).map(UserName::new).orElse(null);
    }

    @Override public Set<BlogDomain> getBlogs() {
        return userDto.getBlogs().stream().map(BlogDomain::new).collect(Collectors.toSet());
    }

    @Override public GuestBook getGuestBook() {
        return Optional.ofNullable(userDto.getGuestBook()).map(GuestBookDomain::new).orElse(null);
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
