package com.github.jactorrises.model.domain.user;

import com.github.jactorrises.client.dto.UserDto;
import com.github.jactorrises.commons.builder.AbstractBuilder;
import com.github.jactorrises.model.domain.person.PersonBuilder;
import com.github.jactorrises.model.domain.person.PersonDomain;

import java.util.Optional;

import static com.github.jactorrises.commons.builder.ValidInstance.collectMessages;
import static com.github.jactorrises.commons.builder.ValidInstance.fetchMessageIfFieldNotPresent;

public final class UserBuilder extends AbstractBuilder<UserDomain> {
    private final UserDto userDto = new UserDto();

    UserBuilder() {
        super(UserBuilder::validateDomain);
    }

    public UserBuilder withUserName(String userName) {
        userDto.setUserName(userName);
        return this;
    }

    public UserBuilder with(PersonDomain personDomain) {
        userDto.setPerson(personDomain.getDto());
        return this;
    }

    public UserBuilder with(PersonBuilder personDomainBuilder) {
        return with(personDomainBuilder.build());
    }

    public UserBuilder withEmailAddress(String emailAddress) {
        userDto.setEmailAddress(emailAddress);
        return this;
    }

    @Override
    protected UserDomain buildBean() {
        return new UserDomain(userDto);
    }

    private static Optional<String> validateDomain(UserDomain userDomain) {
        return collectMessages(
                fetchMessageIfFieldNotPresent("user name", userDomain.getUserName()),
                fetchMessageIfFieldNotPresent("person", userDomain.getPerson())
        );
    }
}
