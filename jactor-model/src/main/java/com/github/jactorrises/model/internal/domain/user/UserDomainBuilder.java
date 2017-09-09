package com.github.jactorrises.model.internal.domain.user;

import com.github.jactorrises.model.internal.domain.DomainBuilder;
import com.github.jactorrises.model.internal.domain.person.PersonDomain;
import com.github.jactorrises.model.internal.domain.person.PersonDomainBuilder;
import com.github.jactorrises.model.internal.persistence.entity.user.UserEntity;

import java.util.Optional;

import static java.util.Arrays.asList;

public final class UserDomainBuilder extends DomainBuilder<UserDomain> {
    static final String THE_FIELD_CANNOT_BE_EMPTY = "The password cannot be empty";
    static final String THE_USER_MUST_BE_A_PERSON = "The user must be a person";
    static final String THE_USER_NAME_CANNOT_BE_NULL = "The user name cannot be null";

    private final UserEntity userEntity = new UserEntity();

    private UserDomainBuilder() {
        super(asList(
                domain -> domain.getUserName() != null ? Optional.empty() : Optional.of(THE_USER_NAME_CANNOT_BE_NULL),
                domain -> domain.getPerson() != null ? Optional.empty() : Optional.of(THE_USER_MUST_BE_A_PERSON),
                domain -> domain.getPassword() != null ? Optional.empty() : Optional.of(THE_FIELD_CANNOT_BE_EMPTY)
        ));
    }

    public UserDomainBuilder withUserName(String userName) {
        userEntity.setUserName(userName);
        return this;
    }

    public UserDomainBuilder with(PersonDomain personDomain) {
        userEntity.setPersonEntity(personDomain.getEntity());
        return this;
    }

    public UserDomainBuilder with(PersonDomainBuilder personDomainBuilder) {
        return with(personDomainBuilder.build());
    }

    public UserDomainBuilder withPassword(String password) {
        userEntity.setPassword(password);
        return this;
    }

    public UserDomainBuilder withEmailAddress(String emailAddress) {
        userEntity.setEmailAddress(emailAddress);
        return this;
    }

    @Override
    protected UserDomain buildBeforeValidation() {
        return new UserDomain(userEntity);
    }

    public static UserDomainBuilder init() {
        return new UserDomainBuilder();
    }
}
