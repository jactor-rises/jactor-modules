package com.github.jactorrises.model.domain.user;

import com.github.jactorrises.client.datatype.EmailAddress;
import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.model.Builder;
import com.github.jactorrises.model.domain.person.PersonBuilder;
import com.github.jactorrises.model.domain.person.PersonDomain;
import com.github.jactorrises.model.persistence.entity.user.UserEntity;

import java.util.Optional;

import static com.github.jactorrises.model.persistence.entity.user.UserEntity.aUser;
import static java.util.Arrays.asList;

public final class UserBuilder extends Builder<UserDomain> {
    static final String THE_FIELD_CANNOT_BE_EMPTY = "The password cannot be empty";
    static final String THE_USER_MUST_BE_A_PERSON = "The user must be a person";
    static final String THE_USER_NAME_CANNOT_BE_NULL = "The user name cannot be null";

    private final UserEntity userEntity = aUser().build();

    UserBuilder() {
        super(asList(
                domain -> domain.getUserName() != null ? Optional.empty() : Optional.of(THE_USER_NAME_CANNOT_BE_NULL),
                domain -> domain.getPerson() != null ? Optional.empty() : Optional.of(THE_USER_MUST_BE_A_PERSON),
                domain -> domain.getPassword() != null ? Optional.empty() : Optional.of(THE_FIELD_CANNOT_BE_EMPTY)
        ));
    }

    public UserBuilder withUserName(String userName) {
        userEntity.setUserName(new UserName(userName));
        return this;
    }

    public UserBuilder with(PersonDomain personDomain) {
        userEntity.setPersonEntity(personDomain.getEntity());
        return this;
    }

    public UserBuilder with(PersonBuilder personDomainBuilder) {
        return with(personDomainBuilder.build());
    }

    public UserBuilder withPassword(String password) {
        userEntity.setPassword(password);
        return this;
    }

    public UserBuilder withEmailAddress(String emailAddress) {
        userEntity.setEmailAddress(new EmailAddress(emailAddress));
        return this;
    }

    @Override
    protected UserDomain buildBean() {
        return new UserDomain(userEntity);
    }

    public static UserDomain build(UserEntity userEntity) {
        return new UserDomain(userEntity);
    }
}
