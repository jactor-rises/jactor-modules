package com.github.jactorrises.model.domain.user;

import com.github.jactorrises.commons.builder.AbstractBuilder;
import com.github.jactorrises.model.domain.person.PersonBuilder;
import com.github.jactorrises.model.domain.person.PersonDomain;
import com.github.jactorrises.persistence.builder.UserEntityBuilder;

import java.util.Optional;

import static com.github.jactorrises.commons.builder.ValidInstance.collectMessages;
import static com.github.jactorrises.commons.builder.ValidInstance.fetchMessageIfFieldNotPresent;
import static com.github.jactorrises.persistence.builder.UserEntityBuilder.aUser;

public final class UserBuilder extends AbstractBuilder<UserDomain> {
    private final UserEntityBuilder userEntityBuilder = aUser();

    UserBuilder() {
        super(UserBuilder::validateDomain);
    }

    public UserBuilder withUserName(String userName) {
        userEntityBuilder.withUserName(userName);
        return this;
    }

    public UserBuilder with(PersonDomain personDomain) {
        userEntityBuilder.with(personDomain.getPersistence());
        return this;
    }

    public UserBuilder with(PersonBuilder personDomainBuilder) {
        return with(personDomainBuilder.build());
    }

    public UserBuilder withEmailAddress(String emailAddress) {
        userEntityBuilder.withEmailAddress(emailAddress);
        return this;
    }

    @Override
    protected UserDomain buildBean() {
        return new UserDomain(userEntityBuilder.build());
    }

    private static Optional<String> validateDomain(UserDomain userDomain) {
        return collectMessages(
                fetchMessageIfFieldNotPresent("user name", userDomain.getUserName()),
                fetchMessageIfFieldNotPresent("person", userDomain.getPerson())
        );
    }
}
