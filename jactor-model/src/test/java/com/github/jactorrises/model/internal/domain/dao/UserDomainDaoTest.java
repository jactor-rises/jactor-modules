package com.github.jactorrises.model.internal.domain.dao;

import com.github.jactorrises.model.internal.domain.person.PersonDomain;
import com.github.jactorrises.model.internal.domain.user.UserDomain;
import com.github.jactorrises.model.internal.domain.user.UserDomainBuilder;
import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.model.internal.persistence.client.dao.UserDao;
import com.github.jactorrises.model.internal.persistence.entity.user.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static com.github.jactorrises.model.internal.domain.address.AddressDomain.anAddress;
import static com.github.jactorrises.model.internal.domain.user.UserDomain.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("a UserDomainDao")
class UserDomainDaoTest {
    @InjectMocks private UserDomainDao userDomainDaoToTest;
    @Mock private UserDao userDaoMock;

    @BeforeEach
    void initMocking() {
        MockitoAnnotations.initMocks(this);
    }

    @DisplayName("should not try to save a user domain being null")
    @Test void willNotSaveNull() {
        userDomainDaoToTest.save(null);
        verify(userDaoMock, never()).save(any());
    }

    @DisplayName("should try to save a user domain not being null")
    @Test void willSaveNotNull() {
        userDomainDaoToTest.save(aValidUser().build());
        verify(userDaoMock, times(1)).save(notNull());
    }

    private UserDomainBuilder aValidUser() {
        return aUser()
                .with(PersonDomain.aPerson()
                        .with(anAddress()
                                .withAddressLine1("on the road")
                                .withZipCode(69)
                                .withCountry("NO")
                        )
                ).withUserName("turbo")
                .withPassword("something");
    }

    @DisplayName("should not try to find the user domain when user name is null")
    @Test void willNotFindUserNameWhichIsNull() {
        assertThat(userDomainDaoToTest.findUsing(null)).isEqualTo(Optional.empty());
        verify(userDaoMock, never()).findUsing(any());
    }

    @DisplayName("should try to find the user domain when user name is not null")
    @Test void willFindUserDomainBasedOnUserName() {
        when(userDaoMock.findUsing(new UserName("someone"))).thenReturn(Optional.of(new UserEntity()));

        userDomainDaoToTest.findUsing(new UserName("someone"));
        verify(userDaoMock, times(1)).findUsing(new UserName("someone"));
    }

    @DisplayName("should not return a user domain if no entity is found")
    @Test void willNotReturnDomainWhenEntityIsNotFound() {
        when(userDaoMock.findUsing(new UserName("someone"))).thenReturn(Optional.empty());
        Optional<UserDomain> userDomain = userDomainDaoToTest.findUsing(new UserName("someone"));

        assertThat(userDomain.isPresent()).as("optional user domain").isEqualTo(false);
    }

    @DisplayName("should return a user domain when an entity is found")
    @Test void willReturnDomainWhenEntityIsFound() {
        when(userDaoMock.findUsing(new UserName("someone"))).thenReturn(Optional.of(new UserEntity()));

        Optional<UserDomain> userDomain = userDomainDaoToTest.findUsing(new UserName("someone"));
        assertThat(userDomain.isPresent()).as("optional user domain").isEqualTo(true);
    }
}
