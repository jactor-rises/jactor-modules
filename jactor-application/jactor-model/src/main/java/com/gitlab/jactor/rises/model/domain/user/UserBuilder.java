package com.gitlab.jactor.rises.model.domain.user;

import com.gitlab.jactor.rises.commons.builder.AbstractBuilder;
import com.gitlab.jactor.rises.commons.builder.MissingFields;
import com.gitlab.jactor.rises.commons.dto.UserDto;
import com.gitlab.jactor.rises.model.domain.person.PersonBuilder;
import com.gitlab.jactor.rises.model.domain.person.PersonDomain;

import java.util.Optional;

public final class UserBuilder extends AbstractBuilder<UserDomain> {
    private final UserDto userDto = new UserDto();

    UserBuilder() {
        super(UserBuilder::validateDomain);
    }

    public UserBuilder withUsername(String username) {
        userDto.setUsername(username);
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

    private static Optional<MissingFields> validateDomain(UserDomain userDomain, MissingFields missingFields) {
        missingFields.addInvalidFieldWhenNoValue(UserDomain.class.getSimpleName(), "username", userDomain.getUsername());
        missingFields.addInvalidFieldWhenNoValue(UserDomain.class.getSimpleName(), "person", userDomain.getPerson());

        return missingFields.presentWhenFieldsAreMissing();
    }
}
